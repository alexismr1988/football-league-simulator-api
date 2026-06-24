package com.alexis.football_league_simulator_api.standings;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StandingsController {

    private final StandingsService standingsService;

    @GetMapping("/leagues/{leagueId}/standings")
    public ResponseEntity<List<StandingsResponse>> getStandingsByLeagueId(@PathVariable Long leagueId){
        List<StandingsResponse> standings = standingsService.generateStandingsByLeagueId(leagueId);

        return new ResponseEntity<>(standings, HttpStatus.OK);
    }
}
