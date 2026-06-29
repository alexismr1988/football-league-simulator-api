package com.alexis.football_league_simulator_api.league;


import com.alexis.football_league_simulator_api.match.Match;
import com.alexis.football_league_simulator_api.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleGeneratorTest {

    private League league;

    @BeforeEach
    void setUp() {
        league = new League();

    }

    @Test
    void shouldGenerateDoubleRoundRobinScheduleForFourteenTeams() {
        addTeams(14);
        ScheduleGenerator scheduleGenerator = new ScheduleGenerator();
        List<Match> schedule = scheduleGenerator.generateSchedule(league);

        assertEquals(182, schedule.size());
        assertTrue(schedule.stream().allMatch(match -> match.getHomeTeam() != null && match.getAwayTeam() != null));
        assertTrue(schedule.stream().allMatch(match-> !match.isPlayed()));
        assertTrue(schedule.stream().allMatch(match -> match.getLeague() == league));
        assertEquals(26, schedule.stream().map(Match::getMatchdayNumber).distinct().count());

    }


    //Comprobar si genera correctamente con un número de equipos impar
    @Test
    void shouldGenerateScheduleForOddNumberOfTeams() {
        addTeams(5);
        ScheduleGenerator scheduleGenerator = new ScheduleGenerator();
        List<Match> schedule = scheduleGenerator.generateSchedule(league);

        assertEquals(20, schedule.size());
        assertTrue(schedule.stream().allMatch(match -> match.getHomeTeam() != null && match.getAwayTeam() != null));
        assertTrue(schedule.stream().allMatch(match-> !match.isPlayed()));
        assertTrue(schedule.stream().allMatch(match -> match.getLeague() == league));
        assertEquals(10, schedule.stream().map(Match::getMatchdayNumber).distinct().count());
        assertTrue(schedule.stream().allMatch(match -> match.getHomeTeam() != match.getAwayTeam()));

    }

    private void addTeams(int numberOfTeams) {
        for (int i = 1; i <= numberOfTeams; i++) {
            Team team = new Team();
            team.setName("Team " + i);
            league.add(team);
        }
    }

}