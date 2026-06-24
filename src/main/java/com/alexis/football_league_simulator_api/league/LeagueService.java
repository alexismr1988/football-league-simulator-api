package com.alexis.football_league_simulator_api.league;

import com.alexis.football_league_simulator_api.league.dto.CreateLeagueRequest;
import com.alexis.football_league_simulator_api.league.dto.LeagueResponse;
import com.alexis.football_league_simulator_api.league.dto.UpdateLeagueRequest;
import com.alexis.football_league_simulator_api.match.Match;
import com.alexis.football_league_simulator_api.match.MatchMapper;
import com.alexis.football_league_simulator_api.match.MatchRepository;
import com.alexis.football_league_simulator_api.match.dto.MatchResponse;
import com.alexis.football_league_simulator_api.team.Team;
import com.alexis.football_league_simulator_api.team.TeamMapper;
import com.alexis.football_league_simulator_api.team.TeamRepository;
import com.alexis.football_league_simulator_api.team.dto.TeamResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final LeagueRepository leagueRepository;
    private final LeagueMapper leagueMapper;
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final ScheduleGenerator scheduleGenerator;
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;

    @Transactional
    public LeagueResponse saveLeague(CreateLeagueRequest createLeagueRequest){
        Set<Long> uniqueTeamIds = new HashSet<>(createLeagueRequest.getTeamIds());

        if (uniqueTeamIds.size() != createLeagueRequest.getTeamIds().size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Team IDs cannot be duplicated"
            );
        }

        List<Team> teams = teamRepository.findAllById(createLeagueRequest.getTeamIds());

        if (teams.size() != createLeagueRequest.getTeamIds().size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "One or more teams do not exist"
            );
        }
        boolean teamAlreadyAssigned = teams.stream()
                .anyMatch(team -> team.getLeague() != null);

        if (teamAlreadyAssigned) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "One or more teams already belong to a league"
            );
        }

        League league = leagueMapper.toEntity(createLeagueRequest, teams);

        League savedLeague = leagueRepository.save(league);

        teams.forEach(team-> team.setLeague(savedLeague));

        return leagueMapper.toResponse(savedLeague);
    }

    public List<LeagueResponse> getAllLeagues(){
        return leagueRepository.findAll().stream()
                .map(leagueMapper::toResponse)
                .toList();
    }

    public LeagueResponse getLeagueById(Long id){
        League league = leagueRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"League not found with id: " + id)
                );

        return leagueMapper.toResponse(league);
    }

    public List<TeamResponse> getTeamsByLeagueId(Long id){
        League league = leagueRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"League not found with id: " + id)
                );

        return league.getTeams().stream()
                .map(teamMapper::toResponse)
                .toList();
    }

    public LeagueResponse updateLeagueName(Long id, UpdateLeagueRequest updateLeagueRequest){
        League league = leagueRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"League not found with id: " + id)
                );
        leagueMapper.updateEntity(updateLeagueRequest, league);

        League updatedLeague = leagueRepository.save(league);

        return leagueMapper.toResponse(updatedLeague);
    }

    @Transactional
    public void deleteLeague(Long id){
        League league = leagueRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"League not found with id: " + id)
                );
        league.getTeams().forEach(team -> {
            team.setLeague(null);
            teamRepository.save(team);
        });

        leagueRepository.delete(league);
    }

    @Transactional
    public List<MatchResponse> generateSchedule(Long leagueId){
        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"League not found with id: " + leagueId)
                );

        if(matchRepository.existsByLeagueId(leagueId)) throw new ResponseStatusException(HttpStatus.CONFLICT,"Schedule already exists");

        List<Match> schedule = scheduleGenerator.generateSchedule(league);

        return matchRepository.saveAll(schedule).stream()
                .map(matchMapper::toResponse)
                .toList();


    }

}
