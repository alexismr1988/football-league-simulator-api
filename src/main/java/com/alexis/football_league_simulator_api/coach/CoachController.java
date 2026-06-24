package com.alexis.football_league_simulator_api.coach;

import com.alexis.football_league_simulator_api.coach.dto.CoachResponse;
import com.alexis.football_league_simulator_api.coach.dto.CreateCoachRequest;
import com.alexis.football_league_simulator_api.coach.dto.UpdateCoachRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CoachController {

    private final CoachService coachService;

    @PostMapping("/coaches")
    public ResponseEntity<CoachResponse> saveCoach(@Valid @RequestBody CreateCoachRequest createCoachRequest){
        CoachResponse savedCoach = coachService.saveCoach(createCoachRequest);

        return new ResponseEntity<>(savedCoach, HttpStatus.CREATED);
    }

    @PostMapping("/teams/{teamId}/coach/{coachId}")
    public ResponseEntity<CoachResponse> assignCoachToTeam(@PathVariable Long teamId, @PathVariable Long coachId){
        CoachResponse savedCoach = coachService.assignCoachToTeam(coachId, teamId);

        return new ResponseEntity<>(savedCoach, HttpStatus.CREATED);
    }

    @GetMapping("teams/{teamId}/coach")
    public ResponseEntity<CoachResponse> getCoachByTeamId(@PathVariable Long teamId){
        CoachResponse coachResponse = coachService.getCoachByTeamId(teamId);

        return ResponseEntity.ok(coachResponse);
    }

    @DeleteMapping("/teams/{teamId}/coach")
    public ResponseEntity<Void> unassignCoachFromTeam(@PathVariable Long teamId){
        coachService.unassignCoachFromTeam(teamId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/coaches/{coachId}")
    public ResponseEntity<CoachResponse> updateCoach(@PathVariable Long coachId, @Valid @RequestBody UpdateCoachRequest updateCoachRequest){
        CoachResponse updatedCoach = coachService.updateCoach(coachId, updateCoachRequest);

        return ResponseEntity.ok(updatedCoach);
    }

    @GetMapping("/coaches")
    public ResponseEntity<List<CoachResponse>> getAllCoaches() {
        return ResponseEntity.ok(coachService.getAllCoaches());
    }
}
