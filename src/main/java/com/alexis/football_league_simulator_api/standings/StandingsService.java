package com.alexis.football_league_simulator_api.standings;

import com.alexis.football_league_simulator_api.exception.ResourceNotFoundException;
import com.alexis.football_league_simulator_api.league.League;
import com.alexis.football_league_simulator_api.league.LeagueRepository;
import com.alexis.football_league_simulator_api.match.Match;
import com.alexis.football_league_simulator_api.match.MatchRepository;
import com.alexis.football_league_simulator_api.team.Team;
import com.alexis.football_league_simulator_api.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Calculates league standings dynamically from played matches.
 * Standings statistics are not persisted.
 */
@Service
@RequiredArgsConstructor
public class StandingsService {

    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final LeagueRepository leagueRepository;


    public List<StandingsResponse> generateStandingsByLeagueId(Long leagueId){
        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("League", leagueId)
                );

        List<StandingsResponse> standings = league.getTeams().stream()
                .map(team -> getStandingsByTeamId(team.getId()))
                        .collect(Collectors.toList());

        // Order by points, goal difference and goals scored.
        standings.sort(Comparator.comparingInt(StandingsResponse::getPoints)
                .thenComparing(StandingsResponse::getGoalDifference)
                .thenComparing(StandingsResponse::getGoalsFor)
                .reversed());

        // Assign positions after applying all tie-break criteria.
        for (int i = 0; i < standings.size(); i++) {
            standings.get(i).setPosition(i + 1);
        }

        return standings;
    }




    private StandingsResponse getStandingsByTeamId(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Team", teamId)
                );

        List<Match> playedMatches= matchRepository.findByHomeTeamIdAndPlayedTrueOrAwayTeamIdAndPlayedTrue(teamId, teamId);

        StandingsResponse standings = calculateTeamStanding(playedMatches, team);

        return standings;
    }

    private StandingsResponse calculateTeamStanding(List<Match> matches, Team team) {
        int points = 0;
        int wins = 0;
        int draws = 0;
        int losses = 0;
        int goalsFor = 0;
        int goalsAgainst = 0;

        for (Match match : matches) {
            boolean isHomeTeam =
                    match.getHomeTeam().getId().equals(team.getId());

            int teamGoals = isHomeTeam
                    ? match.getHomeGoals()
                    : match.getAwayGoals();

            int opponentGoals = isHomeTeam
                    ? match.getAwayGoals()
                    : match.getHomeGoals();

            goalsFor += teamGoals;
            goalsAgainst += opponentGoals;

            if (teamGoals > opponentGoals) {
                wins++;
                points += 3;
            } else if (teamGoals == opponentGoals) {
                draws++;
                points++;
            } else {
                losses++;
            }
        }

        StandingsResponse standing = new StandingsResponse();

        standing.setTeamName(team.getName());
        standing.setPoints(points);
        standing.setGoalsFor(goalsFor);
        standing.setGoalsAgainst(goalsAgainst);
        standing.setGoalDifference(goalsFor - goalsAgainst);
        standing.setWins(wins);
        standing.setDraws(draws);
        standing.setLosses(losses);

        return standing;
    }


}
