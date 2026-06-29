package com.alexis.football_league_simulator_api.lineup;

import com.alexis.football_league_simulator_api.exception.InvalidLineupException;
import com.alexis.football_league_simulator_api.player.Player;
import com.alexis.football_league_simulator_api.player.Position;
import com.alexis.football_league_simulator_api.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class LineupGeneratorTest {

    private LineupGenerator lineupGenerator;
    private Team team;


    @BeforeEach
    void setUp() {
        lineupGenerator = new LineupGenerator();
        team = new Team();

        addPlayers(team, "Goalkeeper", 2, Position.GOALKEEPER);
        addPlayers(team, "Defender", 6, Position.DEFENDER);
        addPlayers(team, "Midfielder", 7, Position.MIDFIELDER);
        addPlayers(team, "Striker", 3, Position.STRIKER);
    }

    @Test
    void generateAutoLineup() {

        Lineup lineup = lineupGenerator.generateAutoLineup(team);

        // Comprobamos el resultado esperado
        assertNotNull(lineup);

        assertEquals(11, lineup.getPlayers().size());
        assertEquals(1, lineup.getGoalkeepers());

        assertTrue(lineup.getDefenders() >= 3);
        assertTrue(lineup.getDefenders() <= 5);

        assertTrue(lineup.getMidfielders() >= 3);
        assertTrue(lineup.getMidfielders() <= 6);

        assertTrue(lineup.getStrikers() >= 1);
        assertTrue(lineup.getStrikers() <= 3);
    }

    @Test
    void generateAutoLineupWithoutGoalkeeper(){
        team.getRoster().clear();

        addPlayers(team, "Defender", 5, Position.DEFENDER);
        addPlayers(team, "Midfielder", 6, Position.MIDFIELDER);
        addPlayers(team, "Striker", 3, Position.STRIKER);


        InvalidLineupException exception = assertThrows(InvalidLineupException.class, () -> lineupGenerator.generateAutoLineup(team));

        assertEquals("The lineup must have only one goalkeeper", exception.getMessage());

    }

    private Player createPlayer(String name, int overall, Position position) {
        Player player = new Player();
        player.setName(name);
        player.setAge(25);
        player.setOverall(overall);
        player.setPosition(position);

        return player;
    }

    private void addPlayers(
            Team team,
            String namePrefix,
            int amount,
            Position position
    ) {
        for (int i = 1; i <= amount; i++) {
            Player player = createPlayer(
                    namePrefix + " " + i,
                    80 - i,
                    position
            );

            team.getRoster().add(player);
        }
    }
}

