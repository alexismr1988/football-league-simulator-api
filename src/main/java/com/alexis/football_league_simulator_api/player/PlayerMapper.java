package com.alexis.football_league_simulator_api.player;

import com.alexis.football_league_simulator_api.player.dto.CreatePlayerRequest;
import com.alexis.football_league_simulator_api.player.dto.PlayerResponse;
import com.alexis.football_league_simulator_api.player.dto.UpdatePlayerRequest;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {


    public PlayerResponse toResponse(Player player){
        PlayerResponse playerResponse = new PlayerResponse();
        playerResponse.setId(player.getId());
        playerResponse.setName(player.getName());
        playerResponse.setAge(player.getAge());
        playerResponse.setOverall(player.getOverall());
        playerResponse.setPosition(player.getPosition());
        playerResponse.setTeamId(player.getTeam().getId());
        playerResponse.setTeamName(player.getTeam().getName());


        return playerResponse;
    }

    public Player toEntity(CreatePlayerRequest createPlayerRequest){
        Player player = new Player();
        player.setName(createPlayerRequest.getName());
        player.setAge(createPlayerRequest.getAge());
        player.setOverall(createPlayerRequest.getOverall());
        player.setPosition(createPlayerRequest.getPosition());

        return player;
    }

    public void updateEntity(UpdatePlayerRequest updatePlayerRequest, Player player) {
        player.setName(updatePlayerRequest.getName());
        player.setAge(updatePlayerRequest.getAge());
        player.setOverall(updatePlayerRequest.getOverall());
        player.setPosition(updatePlayerRequest.getPosition());
    }

}
