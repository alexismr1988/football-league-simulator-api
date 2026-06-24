package com.alexis.football_league_simulator_api.player;

import com.alexis.football_league_simulator_api.player.dto.CreatePlayerRequest;
import com.alexis.football_league_simulator_api.player.dto.PlayerResponse;
import com.alexis.football_league_simulator_api.player.dto.UpdatePlayerRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/teams/{teamId}/players/batch")
    public ResponseEntity<List<PlayerResponse>> createPlayers(@PathVariable Long teamId, @RequestBody List<@Valid CreatePlayerRequest> requests) {
        List<PlayerResponse> createdPlayers = playerService.createPlayers(teamId, requests);

        return new ResponseEntity<>(createdPlayers, HttpStatus.CREATED);
    }

    @GetMapping("/teams/{teamId}/players")
    public ResponseEntity<List<PlayerResponse>> getPlayersByTeamId(@PathVariable Long teamId){
        List<PlayerResponse> players = playerService.getPlayersByTeamId(teamId);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<PlayerResponse> getPlayerById(@PathVariable Long id){
        return ResponseEntity.ok(playerService.getPlayerById(id));
    }

    @PutMapping("/players/{id}")
    public ResponseEntity<PlayerResponse> updatePlayer(@PathVariable Long id, @Valid @RequestBody UpdatePlayerRequest updatePlayerRequest){
        PlayerResponse updatedPlayer = playerService.updatePlayer(id, updatePlayerRequest);
        return ResponseEntity.ok(updatedPlayer);
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id){
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/teams/{teamId}/players")
    public ResponseEntity<PlayerResponse> createPlayer(@PathVariable Long teamId, @Valid @RequestBody CreatePlayerRequest createPlayerRequest) {
        PlayerResponse createdPlayer = playerService.createPlayer(teamId, createPlayerRequest);

        return new ResponseEntity<>(createdPlayer, HttpStatus.CREATED);
    }
}
