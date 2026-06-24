package com.alexis.football_league_simulator_api.league;

import com.alexis.football_league_simulator_api.match.Match;
import com.alexis.football_league_simulator_api.team.Team;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Generates a double round-robin schedule using the circle method.
 * For leagues with an odd number of teams, a null entry represents a bye.
 */
@Component
public class ScheduleGenerator {

    public List<Match> generateSchedule(League league) {
        List<Team> originalTeams = new ArrayList<>(league.getTeams());

        if (originalTeams.size() % 2 != 0) {
            originalTeams.add(null);
        }

        int numberOfTeams = originalTeams.size();
        int numberOfMatchdays = numberOfTeams - 1;
        int matchesPerMatchday = numberOfTeams / 2;

        List<Match> firstLegMatches = new ArrayList<>();

        for (int matchday = 0; matchday < numberOfMatchdays; matchday++) {
            for (int i = 0; i < matchesPerMatchday; i++) {
                Team homeTeam = originalTeams.get(i);
                Team awayTeam = originalTeams.get(numberOfTeams - 1 - i);

                if (homeTeam != null && awayTeam != null) {
                    firstLegMatches.add(createMatch(league, homeTeam, awayTeam, matchday + 1));
                }
            }

            // Keep the first team fixed and rotate the remaining teams.
            List<Team> rotatedTeams = new ArrayList<>();
            rotatedTeams.add(originalTeams.get(0));
            rotatedTeams.add(originalTeams.get(numberOfTeams - 1));

            for (int i = 1; i < numberOfTeams - 1; i++) {
                rotatedTeams.add(originalTeams.get(i));
            }

            originalTeams = rotatedTeams;
        }

        List<Match> schedule = new ArrayList<>(firstLegMatches);

        // Generate the second leg by swapping home and away teams.
        for (Match firstLegMatch : firstLegMatches) {
            schedule.add(createMatch(
                    league,
                    firstLegMatch.getAwayTeam(),
                    firstLegMatch.getHomeTeam(),
                    numberOfMatchdays + firstLegMatch.getMatchdayNumber()
            ));
        }

        return schedule;
    }

    private Match createMatch(League league, Team homeTeam, Team awayTeam, int matchdayNumber) {
        Match match = new Match();
        match.setLeague(league);
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setHomeGoals(0);
        match.setAwayGoals(0);
        match.setMatchdayNumber(matchdayNumber);
        match.setPlayed(false);

        return match;
    }
}
