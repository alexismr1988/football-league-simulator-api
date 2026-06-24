package com.alexis.football_league_simulator_api.lineup;

import com.alexis.football_league_simulator_api.player.Player;
import com.alexis.football_league_simulator_api.player.Position;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@NoArgsConstructor
public class Lineup {

    private Set<Player> players = new HashSet<>();

    public void addPlayer(Player player){
        if(players.size() >= 11) throw new IllegalArgumentException("Lineup already have 11 players.");

        players.add(player);
    }

    public int getGoalkeepers() {
        return (int) players.stream()
                .filter(player -> player.getPosition() == Position.GOALKEEPER)
                .count();
    }

    public int getDefenders(){
        return (int)players.stream()
                .filter(player -> player.getPosition() == Position.DEFENDER ||
                        player.getPosition() == Position.FULL_BACK)
                .count();
    }

    public int getMidfielders(){
        return (int)players.stream()
                .filter(player -> player.getPosition() == Position.ATTACKING_MIDFIELDER ||
                        player.getPosition() == Position.DEFENSIVE_MIDFIELDER ||
                        player.getPosition() == Position.MIDFIELDER)
                .count();
    }

    public int getStrikers(){
        return (int)players.stream()
                .filter(player -> player.getPosition() == Position.STRIKER ||
                        player.getPosition() == Position.SECOND_STRIKER)
                .count();
    }

    public boolean validatePositions(){
        int goalkeepers = getGoalkeepers();
        int defenders = getDefenders();
        int midfielders = getMidfielders();
        int strikers = getStrikers();

        if(goalkeepers != 1) throw new IllegalStateException("The lineup must have only one goalkeeper");
        if(defenders < 3 || defenders > 5) throw new IllegalStateException("The lineup must have between 3 and 5 defenders");
        if(midfielders < 3 || midfielders > 6) throw new IllegalStateException("The lineup must have between 3 and 6 midfielders");
        if(strikers < 1 || strikers > 3) throw new IllegalStateException("The lineup must have between 1 and 3 strikers");

        return true;

    }

    public boolean validateSize(){
        if(players.size() != 11) throw new IllegalArgumentException("The team does not have a valid lineup of 11 players.");

        return true;
    }

    public int averageByPositions(Position... positions) {
        Set<Position> positionSet = Set.of(positions);

        return (int) players.stream()
                .filter(player -> positionSet.contains(player.getPosition()))
                .mapToInt(Player::getOverall)
                .average()
                .orElse(0);
    }

    public double calculateAverage() {
        return players.stream()
                .mapToInt(Player::getOverall)
                .average()
                .orElse(0);
    }

    public String countLines() {
        validateSize();

        return "" + getDefenders() + getMidfielders() + getStrikers();
    }
    public void addTotalPlayers(List<Player> gks, List<Player> dfs, List<Player> mds, List<Player> stks){
        players.addAll(gks);
        players.addAll(dfs);
        players.addAll(mds);
        players.addAll(stks);

    }

}
