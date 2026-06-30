package com.alexis.football_league_simulator_api.simulation;

import com.alexis.football_league_simulator_api.exception.ConflictException;
import com.alexis.football_league_simulator_api.exception.ResourceNotFoundException;
import com.alexis.football_league_simulator_api.league.League;
import com.alexis.football_league_simulator_api.league.LeagueRepository;
import com.alexis.football_league_simulator_api.lineup.Lineup;
import com.alexis.football_league_simulator_api.lineup.LineupGenerator;
import com.alexis.football_league_simulator_api.match.Match;
import com.alexis.football_league_simulator_api.match.MatchMapper;
import com.alexis.football_league_simulator_api.match.MatchRepository;
import com.alexis.football_league_simulator_api.match.dto.MatchResponse;
import com.alexis.football_league_simulator_api.player.Player;
import com.alexis.football_league_simulator_api.player.Position;
import com.alexis.football_league_simulator_api.team.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimulationServiceTest {

    @Mock
    private LineupGenerator lineupGenerator;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchMapper matchMapper;

    @Mock
    private LeagueRepository leagueRepository;

    @InjectMocks
    private SimulationService simulationService;

    @Test
    void shouldThrowExceptionWhenLeagueDoesNotExist() {
        when(leagueRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> simulationService.simulateMatchday(1L, 1));
    }

    @Test
    void shouldThrowExceptionWhenMatchdayContainsPlayedMatch() {
        League league = new League();
        Match match = new Match();
        match.setPlayed(true);
        List<Match> matches = List.of(match);
        when(leagueRepository.findById(1L)).thenReturn(Optional.of(league));
        when(matchRepository.findByLeagueIdAndMatchdayNumber(1L, 1)).thenReturn(matches);
        assertThrows(ConflictException.class, () -> simulationService.simulateMatchday(1L, 1));

    }

    @Test
    void testSimulateMatchday() {
        League league = new League();
        Team homeTeam = new Team();
        homeTeam.setName("Home Team");

        Team awayTeam = new Team();
        awayTeam.setName("Away Team");

        Lineup homeLineup = createValidLineup();
        Lineup awayLineup = createValidLineup();

        Match match = new Match();
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setPlayed(false);

        List<Match> matches = List.of(match);

        MatchResponse expectedResponse = new MatchResponse(
                1L,
                "Test League",
                1,
                homeTeam.getName(),
                awayTeam.getName(),
                "0 - 0",
                true
        );

        when(lineupGenerator.generateAutoLineup(homeTeam))
                .thenReturn(homeLineup);

        when(lineupGenerator.generateAutoLineup(awayTeam))
                .thenReturn(awayLineup);

        when(matchRepository.save(match))
                .thenReturn(match);

        when(matchMapper.toResponse(match))
                .thenReturn(expectedResponse);

        when(leagueRepository.findById(1L)).thenReturn(Optional.of(league));
        when(matchRepository.findByLeagueIdAndMatchdayNumber(1L, 1)).thenReturn(matches);

        List<MatchResponse> actualResponses = simulationService.simulateMatchday(1L, 1);
        assertEquals(List.of(expectedResponse), actualResponses);
        assertTrue(actualResponses.stream().allMatch(MatchResponse::isPlayed));
        assertTrue(match.isPlayed());
    }

    private Lineup createValidLineup() {
        Lineup lineup = new Lineup();

        addPlayers(lineup, 1, Position.GOALKEEPER);
        addPlayers(lineup, 4, Position.DEFENDER);
        addPlayers(lineup, 4, Position.MIDFIELDER);
        addPlayers(lineup, 2, Position.STRIKER);

        return lineup;
    }

    private void addPlayers(Lineup lineup, int amount, Position position) {
        for (int i = 0; i < amount; i++) {
            Player player = new Player();
            player.setName(position + " " + i);
            player.setAge(25);
            player.setOverall(80);
            player.setPosition(position);

            lineup.addPlayer(player);
        }
    }

}