package com.alexis.football_league_simulator_api.league;

import com.alexis.football_league_simulator_api.league.dto.CreateLeagueRequest;
import com.alexis.football_league_simulator_api.league.dto.LeagueResponse;
import com.alexis.football_league_simulator_api.league.dto.UpdateLeagueRequest;
import com.alexis.football_league_simulator_api.match.dto.MatchResponse;
import com.alexis.football_league_simulator_api.team.dto.TeamResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/leagues")
public class LeagueController {

    private final LeagueService leagueService;

    @PostMapping("")
    public ResponseEntity<LeagueResponse> saveLeague(@Valid @RequestBody CreateLeagueRequest createLeagueRequest){
        LeagueResponse savedLeague = leagueService.saveLeague(createLeagueRequest);

        return new ResponseEntity<>(savedLeague, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<LeagueResponse>> gettAllLeagues(){
        return ResponseEntity.ok(leagueService.getAllLeagues());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeagueResponse> getLeagueById(@PathVariable Long id){
        LeagueResponse leagueResponse = leagueService.getLeagueById(id);

        return ResponseEntity.ok(leagueResponse);
    }

    @GetMapping("/{id}/teams")
    public ResponseEntity<List<TeamResponse>> getTeamsByLeagueId(@PathVariable Long id){
        List<TeamResponse> teams = leagueService.getTeamsByLeagueId(id);

        return ResponseEntity.ok(teams);

    }

    @PutMapping("/{id}")
    public ResponseEntity<LeagueResponse> updateLeague(@PathVariable Long id, @Valid  @RequestBody UpdateLeagueRequest updateLeagueRequest){
        LeagueResponse updatedLeague = leagueService.updateLeagueName(id, updateLeagueRequest);

        return ResponseEntity.ok(updatedLeague);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeague(@PathVariable Long id){
        leagueService.deleteLeague(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{leagueId}/schedule")
    public ResponseEntity<List<MatchResponse>> generateSchedule(@PathVariable Long leagueId){
        List<MatchResponse> schedule = leagueService.generateSchedule(leagueId);

        return ResponseEntity.status(HttpStatus.CREATED).body(schedule);
    }
}
