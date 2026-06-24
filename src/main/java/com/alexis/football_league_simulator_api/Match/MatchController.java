package com.alexis.football_league_simulator_api.match;

import com.alexis.football_league_simulator_api.match.dto.MatchResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/leagues/{leagueId}/matches")
    public ResponseEntity<List<MatchResponse>> getMatchesByLeagueId(@PathVariable Long leagueId) {
        List<MatchResponse> matches = matchService.getMatchesByLeagueId(leagueId);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/leagues/{leagueId}/matches/played")
    public ResponseEntity<List<MatchResponse>> getPlayedMatchesByLeagueId(@PathVariable Long leagueId) {
        List<MatchResponse> matches = matchService.getPlayedMatchesByLeagueId(leagueId);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/leagues/{leagueId}/matches/pending")
    public ResponseEntity<List<MatchResponse>> getNonPlayedMatchesByLeagueId(@PathVariable Long leagueId) {
        List<MatchResponse> matches = matchService.getNonPlayedMatchesByLeagueId(leagueId);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/teams/{teamId}/matches")
    public ResponseEntity<List<MatchResponse>> getMatchesByTeamId(@PathVariable Long teamId) {
        List<MatchResponse> matches = matchService.getMatchesByTeamId(teamId);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/teams/{teamId}/matches/played")
    public ResponseEntity<List<MatchResponse>> getPlayedMatchesByTeamId(@PathVariable Long teamId) {
        List<MatchResponse> matches = matchService.getPlayedMatchesByTeamId(teamId);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/teams/{teamId}/matches/pending")
    public ResponseEntity<List<MatchResponse>> getNonPlayedMatchesByTeamId(@PathVariable Long teamId) {
        List<MatchResponse> matches = matchService.getNonPlayedMatchesByTeamId(teamId);
        return ResponseEntity.ok(matches);
    }


}
