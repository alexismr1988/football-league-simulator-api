package com.alexis.football_league_simulator_api.simulation;

import com.alexis.football_league_simulator_api.match.dto.MatchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    @PostMapping("leagues/{leagueId}/matchdays/{matchdayNumber}/simulate")
    public ResponseEntity<List<MatchResponse>> simulateMatchday(@PathVariable Long leagueId, @PathVariable int matchdayNumber){
        List<MatchResponse> matches = simulationService.simulateMatchday(leagueId, matchdayNumber);

        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

}
