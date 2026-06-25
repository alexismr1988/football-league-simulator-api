package com.alexis.football_league_simulator_api.simulation;

import com.alexis.football_league_simulator_api.coach.Coach;
import com.alexis.football_league_simulator_api.coach.CoachStyle;
import com.alexis.football_league_simulator_api.league.LeagueRepository;
import com.alexis.football_league_simulator_api.lineup.Lineup;
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

    private static final String MATCH_ALREADY_SIMULATED_MESSAGE = "The match is already simulated";
    private static final double HOME_BASE_GOAL_PROBABILITY = 0.18;
    private static final double AWAY_BASE_GOAL_PROBABILITY = 0.15;
    private static final int BASE_CHANCES = 12;
    private static final int STANDARD_DEFENDERS = 4;
    private static final int LOW_MIDFIELDERS = 3;
    private static final int HIGH_MIDFIELDERS = 5;
    private static final int VERY_HIGH_MIDFIELDERS = 6;
    private static final int SINGLE_STRIKER = 1;
    private static final int THREE_STRIKERS = 3;
    private static final double WEAK_DEFENSE_GOAL_MULTIPLIER = 1.15;
    private static final double STRONG_DEFENSE_GOAL_MULTIPLIER = 0.85;
    private static final int LOW_MIDFIELD_CHANCE_PENALTY = 2;
    private static final int HIGH_MIDFIELD_CHANCE_BONUS = 4;
    private static final int VERY_HIGH_MIDFIELD_CHANCE_BONUS = 6;
    private static final double SINGLE_STRIKER_GOAL_MULTIPLIER = 0.85;
    private static final double THREE_STRIKERS_GOAL_MULTIPLIER = 1.10;
    private static final double RATING_PERCENT_DIVISOR = 100.0;
    private static final double GOALKEEPER_RATING_REDUCTION_FACTOR = 0.05;
    private static final double BASE_GOALKEEPER_GOAL_REDUCTION = 0.03;
    private static final int LINE_RATING_CHANCE_DIVISOR = 20;
    private static final double STRIKER_RATING_GOAL_BONUS_FACTOR = 0.09;
    private static final double BASE_STRIKER_GOAL_BONUS = 0.01;
    private static final double DEFENSIVE_COACH_GOAL_MULTIPLIER = 0.95;
    private static final double ATTACKING_COACH_GOAL_MULTIPLIER = 1.05;

    private final LineupGenerator lineupGenerator;
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final LeagueRepository leagueRepository;

    private Match simulateMatch(Match match) {

        if (match.isPlayed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MATCH_ALREADY_SIMULATED_MESSAGE);
        }

        Team homeTeam = match.getHomeTeam();
        Team awayTeam = match.getAwayTeam();

        homeTeam.setLineup(lineupGenerator.generateAutoLineup(homeTeam));
        awayTeam.setLineup(lineupGenerator.generateAutoLineup(awayTeam));
        Lineup homeLineup = homeTeam.getLineup();
        Lineup awayLineup = awayTeam.getLineup();

        // Base probabilities already include a small home advantage.
        double homeGoalProbability = HOME_BASE_GOAL_PROBABILITY;
        double awayGoalProbability = AWAY_BASE_GOAL_PROBABILITY;
        int homeChances = BASE_CHANCES;
        int awayChances = BASE_CHANCES;
        Coach homeCoach = homeTeam.getCoach();
        Coach awayCoach = awayTeam.getCoach();
        String homeFormation = homeLineup.countLines();
        String awayFormation = awayLineup.countLines();

        if (homeLineup.getDefenders() < STANDARD_DEFENDERS) awayGoalProbability *= WEAK_DEFENSE_GOAL_MULTIPLIER;
        if (homeLineup.getDefenders() > STANDARD_DEFENDERS) awayGoalProbability *= STRONG_DEFENSE_GOAL_MULTIPLIER;
        if (homeLineup.getMidfielders() == LOW_MIDFIELDERS) homeChances -= LOW_MIDFIELD_CHANCE_PENALTY;
        if (homeLineup.getMidfielders() == HIGH_MIDFIELDERS) homeChances += HIGH_MIDFIELD_CHANCE_BONUS;
        if (homeLineup.getMidfielders() == VERY_HIGH_MIDFIELDERS) homeChances += VERY_HIGH_MIDFIELD_CHANCE_BONUS;
        if (homeLineup.getStrikers() == SINGLE_STRIKER) homeGoalProbability *= SINGLE_STRIKER_GOAL_MULTIPLIER;
        if (homeLineup.getStrikers() == THREE_STRIKERS) homeGoalProbability *= THREE_STRIKERS_GOAL_MULTIPLIER;

        if (awayLineup.getDefenders() < STANDARD_DEFENDERS) homeGoalProbability *= WEAK_DEFENSE_GOAL_MULTIPLIER;
        if (awayLineup.getDefenders() > STANDARD_DEFENDERS) homeGoalProbability *= STRONG_DEFENSE_GOAL_MULTIPLIER;
        if (awayLineup.getMidfielders() == LOW_MIDFIELDERS) awayChances -= LOW_MIDFIELD_CHANCE_PENALTY;
        if (awayLineup.getMidfielders() == HIGH_MIDFIELDERS) awayChances += HIGH_MIDFIELD_CHANCE_BONUS;
        if (awayLineup.getMidfielders() == VERY_HIGH_MIDFIELDERS) awayChances += VERY_HIGH_MIDFIELD_CHANCE_BONUS;
        if (awayLineup.getStrikers() == SINGLE_STRIKER) awayGoalProbability *= SINGLE_STRIKER_GOAL_MULTIPLIER;
        if (awayLineup.getStrikers() == THREE_STRIKERS) awayGoalProbability *= THREE_STRIKERS_GOAL_MULTIPLIER;

        // Medias del equipo local
        int homeGoalkeeperAverage = homeLineup.averageByPositions(Position.GOALKEEPER);
        int homeDefendersAverage = homeLineup.averageByPositions(Position.DEFENDER, Position.FULL_BACK);
        int homeMidfieldersAverage = homeLineup.averageByPositions(Position.DEFENSIVE_MIDFIELDER, Position.MIDFIELDER, Position.ATTACKING_MIDFIELDER);
        int homeStrikersAverage = homeLineup.averageByPositions(Position.STRIKER, Position.SECOND_STRIKER);

        // Medias del equipo visitante
        int awayGoalkeeperAverage = awayLineup.averageByPositions(Position.GOALKEEPER);
        int awayDefendersAverage = awayLineup.averageByPositions(Position.DEFENDER, Position.FULL_BACK);
        int awayMidfieldersAverage = awayLineup.averageByPositions(Position.DEFENSIVE_MIDFIELDER, Position.MIDFIELDER, Position.ATTACKING_MIDFIELDER);
        int awayStrikersAverage = awayLineup.averageByPositions(Position.STRIKER, Position.SECOND_STRIKER);

        // Adjust chances and scoring probabilities according to each positional line.
        double homeGoalReduction = (homeGoalkeeperAverage / RATING_PERCENT_DIVISOR)
                * GOALKEEPER_RATING_REDUCTION_FACTOR
                + BASE_GOALKEEPER_GOAL_REDUCTION;
        int awayChancePenalty = homeDefendersAverage / LINE_RATING_CHANCE_DIVISOR;
        int homeChanceIncrease = homeMidfieldersAverage / LINE_RATING_CHANCE_DIVISOR;
        double homeExtraGoalPercentage = (homeStrikersAverage / RATING_PERCENT_DIVISOR)
                * STRIKER_RATING_GOAL_BONUS_FACTOR
                + BASE_STRIKER_GOAL_BONUS;

        double awayGoalReduction = (awayGoalkeeperAverage / RATING_PERCENT_DIVISOR)
                * GOALKEEPER_RATING_REDUCTION_FACTOR
                + BASE_GOALKEEPER_GOAL_REDUCTION;
        int homeChancePenalty = awayDefendersAverage / LINE_RATING_CHANCE_DIVISOR;
        int awayChanceIncrease = awayMidfieldersAverage / LINE_RATING_CHANCE_DIVISOR;
        double awayExtraGoalPercentage = (awayStrikersAverage / RATING_PERCENT_DIVISOR)
                * STRIKER_RATING_GOAL_BONUS_FACTOR
                + BASE_STRIKER_GOAL_BONUS;

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
                    case DEFENSIVE -> awayGoalProbability *= DEFENSIVE_COACH_GOAL_MULTIPLIER;
                    case OFFENSIVE, POSSESSION, COUNTER_ATTACK -> homeGoalProbability *= ATTACKING_COACH_GOAL_MULTIPLIER;
                }
            }
        }

        if (awayCoach != null) {
            CoachStyle awayCoachStyle = awayCoach.getCoachStyle();

            if (awayCoachStyle.getFormations().contains(awayFormation)) {
                switch (awayCoachStyle) {
                    case DEFENSIVE -> homeGoalProbability *= DEFENSIVE_COACH_GOAL_MULTIPLIER;
                    case OFFENSIVE, POSSESSION, COUNTER_ATTACK -> awayGoalProbability *= ATTACKING_COACH_GOAL_MULTIPLIER;
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

        return matchRepository.save(match);
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
