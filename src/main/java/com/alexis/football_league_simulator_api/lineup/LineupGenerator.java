package com.alexis.football_league_simulator_api.lineup;

import com.alexis.football_league_simulator_api.player.Player;
import com.alexis.football_league_simulator_api.player.Position;
import com.alexis.football_league_simulator_api.team.Team;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class LineupGenerator {

    public Lineup generateAutoLineup(Team team) {
        List<Player> roster = new ArrayList<>(team.getRoster());
        Lineup lineup = new Lineup();

        boolean isValid = false;
        int[] formation = new int[3];

        while (!isValid) {
            int defenders = randomValue(3, 5);
            int midfielders = randomValue(3, 6);
            int strikers = randomValue(1, 3);

            if (defenders + midfielders + strikers == 10) {
                formation[0] = defenders;
                formation[1] = midfielders;
                formation[2] = strikers;
                isValid = true;
            }
        }

        int numDefenders = formation[0];
        int numMids = formation[1];
        int numStrikers = formation[2];

        roster.sort(Comparator.comparingInt(Player::getOverall).reversed());

        List<Player> goalkeepers = new ArrayList<>();
        List<Player> defenders = new ArrayList<>();
        List<Player> midfielders = new ArrayList<>();
        List<Player> strikers = new ArrayList<>();

        roster.forEach(player -> {
            Position position = player.getPosition();

            if (position == Position.GOALKEEPER && goalkeepers.size() < 1) {
                goalkeepers.add(player);
            }

            if ((position == Position.DEFENDER || position == Position.FULL_BACK)
                    && defenders.size() < numDefenders) {
                defenders.add(player);
            }

            if ((position == Position.MIDFIELDER
                    || position == Position.DEFENSIVE_MIDFIELDER
                    || position == Position.ATTACKING_MIDFIELDER)
                    && midfielders.size() < numMids) {
                midfielders.add(player);
            }

            if ((position == Position.STRIKER || position == Position.SECOND_STRIKER)
                    && strikers.size() < numStrikers) {
                strikers.add(player);
            }
        });

        lineup.addTotalPlayers(goalkeepers, defenders, midfielders, strikers);

        //Relleno si la alineación tiene menos de 11 jugadores
        if(lineup.getPlayers().size() < 11){
            int actualStrikers = lineup.getStrikers();
            int actualMidfielders = lineup.getMidfielders();
            int actualDefenders = lineup.getDefenders();

            for (Player player : roster) {
                if (lineup.getPlayers().contains(player)) {
                    continue;
                }

                Position position = player.getPosition();

                if (position == Position.GOALKEEPER) {
                    continue;
                }

                if ((position == Position.STRIKER || position == Position.SECOND_STRIKER)
                        && actualStrikers < 3) {
                    lineup.addPlayer(player);
                    actualStrikers++;
                } else if ((position == Position.MIDFIELDER
                        || position == Position.DEFENSIVE_MIDFIELDER
                        || position == Position.ATTACKING_MIDFIELDER)
                        && actualMidfielders < 6) {
                    lineup.addPlayer(player);
                    actualMidfielders++;
                } else if ((position == Position.DEFENDER || position == Position.FULL_BACK)
                        && actualDefenders < 5) {
                    lineup.addPlayer(player);
                    actualDefenders++;
                }

                if (lineup.getPlayers().size() == 11) {
                    break;
                }
            }

        }

        lineup.validateSize();
        lineup.validatePositions();

        return lineup;
    }

    //Método auxiliar usado en generar alineaciones automáticas
    public int randomValue(int min, int max) {
        return (int)(Math.random() * (max - min + 1)) + min;
    }
}
