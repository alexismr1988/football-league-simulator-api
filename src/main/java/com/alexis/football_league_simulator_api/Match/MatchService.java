package com.alexis.football_league_simulator_api.match;

import com.alexis.football_league_simulator_api.league.LeagueRepository;
import com.alexis.football_league_simulator_api.match.dto.MatchResponse;
import com.alexis.football_league_simulator_api.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final MatchMapper matchMapper;

    public Match save(Match match) {
        return matchRepository.save(match);
    }

    public List<Match> saveAll(List<Match> matches) {
        return matchRepository.saveAll(matches);
    }

    public List<MatchResponse> getMatchesByLeagueId(Long leagueId) {
        leagueRepository.findById(leagueId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"League not found with id: " + leagueId)
                );

        return matchRepository.findByLeagueId(leagueId).stream()
                .map(matchMapper::toResponse)
                .toList();
    }

    public List<MatchResponse> getPlayedMatchesByLeagueId(Long leagueId) {
        leagueRepository.findById(leagueId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"League not found with id: " + leagueId)
                );

        return matchRepository.findByLeagueIdAndPlayedTrue(leagueId).stream()
                .map(matchMapper::toResponse)
                .toList();
    }

    public List<MatchResponse> getNonPlayedMatchesByLeagueId(Long leagueId) {
        leagueRepository.findById(leagueId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"League not found with id: " + leagueId)
                );

        return matchRepository.findByLeagueIdAndPlayedFalse(leagueId).stream()
                .map(matchMapper::toResponse)
                .toList();
    }

    public List<MatchResponse> getMatchesByTeamId(Long teamId) {
        teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Team not found with id: " + teamId)
                );
        return matchRepository.findByHomeTeamIdOrAwayTeamId(teamId, teamId).stream()
                .map(matchMapper::toResponse)
                .toList();
    }



    public List<MatchResponse> getPlayedMatchesByTeamId(Long teamId) {
        teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Team not found with id: " + teamId)
                );
        return matchRepository.findByHomeTeamIdAndPlayedTrueOrAwayTeamIdAndPlayedTrue(teamId, teamId).stream()
                .map(matchMapper::toResponse)
                .toList();
    }

    public List<MatchResponse> getNonPlayedMatchesByTeamId(Long teamId) {
        teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Team not found with id: " + teamId)
                );
        return matchRepository.findByHomeTeamIdAndPlayedFalseOrAwayTeamIdAndPlayedFalse(teamId, teamId).stream()
                .map(matchMapper::toResponse)
                .toList();
    }


}
