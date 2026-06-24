package com.alexis.football_league_simulator_api.team;

import com.alexis.football_league_simulator_api.team.dto.CreateTeamRequest;
import com.alexis.football_league_simulator_api.team.dto.TeamResponse;
import com.alexis.football_league_simulator_api.team.dto.UpdateTeamRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamResponse> saveTeam( @Valid @RequestBody CreateTeamRequest createTeamRequest) {
        TeamResponse createdTeam = teamService.saveTeam(createTeamRequest);

        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeam(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTeamRequest updateTeamRequest
    ) {
        TeamResponse updatedTeam =
                teamService.updateTeam(id, updateTeamRequest);

        return ResponseEntity.ok(updatedTeam);
    }

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeamById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }
}