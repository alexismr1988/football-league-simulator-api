package com.alexis.football_league_simulator_api.player;

import com.alexis.football_league_simulator_api.player.dto.CreatePlayerRequest;
import com.alexis.football_league_simulator_api.player.dto.PlayerResponse;
import com.alexis.football_league_simulator_api.player.dto.UpdatePlayerRequest;
import com.alexis.football_league_simulator_api.team.Team;
import com.alexis.football_league_simulator_api.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final TeamRepository teamRepository;


    public PlayerResponse createPlayer(Long teamId, CreatePlayerRequest createPlayerRequest) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Team not found with id: " + teamId
                        )
                );

        Player player = playerMapper.toEntity(createPlayerRequest);

        player.setTeam(team);

        Player savedPlayer = playerRepository.save(player);

        return playerMapper.toResponse(savedPlayer);
    }


    public PlayerResponse updatePlayer(Long id, UpdatePlayerRequest updatePlayerRequest){
        Player player = playerRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Player not found with id: " + id)
                );
        playerMapper.updateEntity(updatePlayerRequest, player);
        Player updatedPlayer = playerRepository.save(player);
        return playerMapper.toResponse(updatedPlayer);
    }

    public void deletePlayer(Long id){
        Player player = playerRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Player not found with id: " + id)
                );

        playerRepository.delete(player);
    }

    public List<PlayerResponse> getAllPlayers(){
        return playerRepository.findAll()
                .stream()
                .map(playerMapper::toResponse)
                .toList();
    }

    public PlayerResponse getPlayerById(Long id){
        Player player = playerRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Player not found with id: " + id)
                );
        return playerMapper.toResponse(player);
    }

    public List<PlayerResponse> getPlayersByTeamId(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Team not found with id: " + teamId)
                );
        return playerRepository.findByTeamId(teamId).stream()
                .map(playerMapper::toResponse)
                .toList();
    }

    public List<PlayerResponse> createPlayers(Long teamId, List<CreatePlayerRequest> requests) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Team not found with id: " + teamId
                        )
                );

        List<Player> players = requests.stream()
                .map(playerMapper::toEntity)
                .peek(player -> player.setTeam(team))
                .toList();

        List<Player> savedPlayers = playerRepository.saveAll(players);

        return savedPlayers.stream()
                .map(playerMapper::toResponse)
                .toList();
    }



}
