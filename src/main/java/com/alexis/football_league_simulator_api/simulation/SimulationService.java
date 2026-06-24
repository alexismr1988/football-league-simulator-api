package com.alexis.football_league_simulator_api.simulation;

import com.alexis.football_league_simulator_api.coach.Coach;
import com.alexis.football_league_simulator_api.coach.CoachStyle;
import com.alexis.football_league_simulator_api.league.LeagueRepository;
import com.alexis.football_league_simulator_api.lineup.LineupGenerator;
import com.alexis.football_league_simulator_api.match.Match;
import com.alexis.football_league_simulator_api.match.MatchMapper;
import com.alexis.football_league_simulator_api.match.MatchRepository;
import com.alexis.football_league_simulator_api.match.dto.MatchResponse;
import com.alexis.football_league_simulator_api.player.Position;
import com.alexis.football_league_simulator_api.team.Team;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulates football matches using automatic lineups, player ratings,
 * tactical formations, coach styles and random goal probabilities.
 */
@Service
@RequiredArgsConstructor
public class SimulationService {

    private final LineupGenerator lineupGenerator;
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final LeagueRepository leagueRepository;

    private Match simulateMatch(Match match){

        if(match.isPlayed()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The match is already simulated");

        Team homeTeam = match.getHomeTeam();
        Team awayTeam = match.getAwayTeam();

        homeTeam.setLineup(lineupGenerator.generateAutoLineup(homeTeam));
        awayTeam.setLineup(lineupGenerator.generateAutoLineup(awayTeam));

        // Base probabilities already include a small home advantage.
        double homeGoalProbability = 0.18;  // Probabilidad de gol por ocasión
        double awayGoalProbability = 0.15;   // Probabilidad de gol por ocasión
        int homeChances = 12;
        int awayChances = 12;
        Coach homeCoach = homeTeam.getCoach();
        Coach awayCoach = awayTeam.getCoach();
        String homeFormation = homeTeam.getLineup().countLines();
        String awayFormation = awayTeam.getLineup().countLines();

        if (homeTeam.getLineup().getDefenders() < 4) awayGoalProbability *= 1.15;
        if (homeTeam.getLineup().getDefenders() > 4) awayGoalProbability *= 0.85;
        if (homeTeam.getLineup().getMidfielders() == 3) homeChances -= 2;
        if (homeTeam.getLineup().getMidfielders() == 5) homeChances += 4;
        if (homeTeam.getLineup().getMidfielders() == 6) homeChances += 6;
        if (homeTeam.getLineup().getStrikers() == 1) homeGoalProbability *= 0.85;
        if (homeTeam.getLineup().getStrikers() == 3) homeGoalProbability *= 1.10;

        if (awayTeam.getLineup().getDefenders() < 4) homeGoalProbability *= 1.15;
        if (awayTeam.getLineup().getDefenders() > 4) homeGoalProbability *= 0.85;
        if (awayTeam.getLineup().getMidfielders() == 3) awayChances -= 2;
        if (awayTeam.getLineup().getMidfielders() == 5) awayChances += 4;
        if (awayTeam.getLineup().getMidfielders() == 6) awayChances += 6;
        if (awayTeam.getLineup().getStrikers() == 1) awayGoalProbability *= 0.85;
        if (awayTeam.getLineup().getStrikers() == 3) awayGoalProbability *= 1.10;

        // Medias del equipo local
        int homeGoalkeeperAverage = homeTeam.getLineup().averageByPositions(Position.GOALKEEPER);
        int homeDefendersAverage = homeTeam.getLineup().averageByPositions(Position.DEFENDER, Position.FULL_BACK);
        int homeMidfieldersAverage = homeTeam.getLineup().averageByPositions(Position.DEFENSIVE_MIDFIELDER, Position.MIDFIELDER, Position.ATTACKING_MIDFIELDER);
        int homeStrikersAverage = homeTeam.getLineup().averageByPositions(Position.STRIKER, Position.SECOND_STRIKER);

        // Medias del equipo visitante
        int awayGoalkeeperAverage = awayTeam.getLineup().averageByPositions(Position.GOALKEEPER);
        int awayDefendersAverage = awayTeam.getLineup().averageByPositions(Position.DEFENDER, Position.FULL_BACK);
        int awayMidfieldersAverage = awayTeam.getLineup().averageByPositions(Position.DEFENSIVE_MIDFIELDER, Position.MIDFIELDER, Position.ATTACKING_MIDFIELDER);
        int awayStrikersAverage = awayTeam.getLineup().averageByPositions(Position.STRIKER, Position.SECOND_STRIKER);

        // Adjust chances and scoring probabilities according to each positional line.
        double homeGoalReduction = (homeGoalkeeperAverage / 100.0) * 0.05 + 0.03;
        int awayChancePenalty = homeDefendersAverage / 20;
        int homeChanceIncrease = homeMidfieldersAverage / 20;
        double homeExtraGoalPercentage = (homeStrikersAverage / 100.0) * 0.09 + 0.01;

        double awayGoalReduction = (awayGoalkeeperAverage / 100.0) * 0.05 + 0.03;
        int homeChancePenalty = awayDefendersAverage / 20;
        int awayChanceIncrease = awayMidfieldersAverage / 20;
        double awayExtraGoalPercentage = (awayStrikersAverage / 100.0) * 0.09 + 0.01;

        // Each chance is evaluated independently using the final scoring probability.
        awayGoalProbability *= 1 - homeGoalReduction;
        awayChances -= awayChancePenalty;
        homeChances += homeChanceIncrease;
        homeGoalProbability *= 1 + homeExtraGoalPercentage;

        homeGoalProbability *= 1 - awayGoalReduction;
        homeChances -= homeChancePenalty;
        awayChances += awayChanceIncrease;
        awayGoalProbability *= 1 + awayExtraGoalPercentage;

        if (homeCoach != null) {
            CoachStyle homeCoachStyle = homeCoach.getCoachStyle();

            if (homeCoachStyle.getFormations().contains(homeFormation)) {
                switch (homeCoachStyle) {
                    case DEFENSIVE -> awayGoalProbability *= 0.95;
                    case OFFENSIVE, POSSESSION, COUNTER_ATTACK -> homeGoalProbability *= 1.05;
                }
            }
        }

        if (awayCoach != null) {
            CoachStyle awayCoachStyle = awayCoach.getCoachStyle();

            if (awayCoachStyle.getFormations().contains(awayFormation)) {
                switch (awayCoachStyle) {
                    case DEFENSIVE -> homeGoalProbability *= 0.95;
                    case OFFENSIVE, POSSESSION, COUNTER_ATTACK -> awayGoalProbability *= 1.05;
                }
            }
        }


        // Simulate goals
        int homeGoals = 0;

        for (int i = 0; i < homeChances; i++) {
            if (Math.random() < homeGoalProbability) {
                homeGoals++;
            }
        }

        int awayGoals = 0;

        for (int i = 0; i < awayChances; i++) {
            if (Math.random() < awayGoalProbability) {
                awayGoals++;
            }
        }

        match.setHomeGoals(homeGoals);
        match.setAwayGoals(awayGoals);
        match.setPlayed(true);

        Match savedMatch = matchRepository.save(match);

        return savedMatch;

    }

    // The whole matchday is simulated inside one transaction to avoid partial results.
    @Transactional
    public List<MatchResponse> simulateMatchday(Long leagueId,int matchday){
        leagueRepository.findById(leagueId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"League not found with id: " + leagueId)
                );

        List<Match> matches = matchRepository.findByLeagueIdAndMatchdayNumber(leagueId, matchday);
        if (matches.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No matches found for league id: " + leagueId + " and matchday: " + matchday
            );
        }

        matches.forEach(match -> {
            if(match.isPlayed()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Matchday is already simulated");
        });

        List<Match> simulatedMatches = new ArrayList<>();
        matches.forEach(match -> {
            Match simulatedMatch = simulateMatch(match);
            simulatedMatches.add(simulatedMatch);
        });

        return simulatedMatches.stream()
                .map(matchMapper::toResponse)
                .toList();
    }

}
