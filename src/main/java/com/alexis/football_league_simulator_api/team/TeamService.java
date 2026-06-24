package com.alexis.football_league_simulator_api.team;

import com.alexis.football_league_simulator_api.team.dto.CreateTeamRequest;
import com.alexis.football_league_simulator_api.team.dto.TeamResponse;
import com.alexis.football_league_simulator_api.team.dto.UpdateTeamRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public TeamResponse saveTeam(CreateTeamRequest createTeamRequest) {
        Team team = teamMapper.toEntity(createTeamRequest);

        Team savedTeam = teamRepository.save(team);

        return teamMapper.toResponse(savedTeam);
    }

    public TeamResponse updateTeam(Long id, UpdateTeamRequest updateTeamRequest) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Team not found with id: " + id)
                );

        teamMapper.updateEntity(updateTeamRequest, team);

        Team updatedTeam = teamRepository.save(team);

        return teamMapper.toResponse(updatedTeam);
    }

    public TeamResponse getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Team not found with id: " + id)
                );

        return teamMapper.toResponse(team);
    }

    public void deleteTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Team not found with id: " + id)
                );

        teamRepository.delete(team);
    }

    public List<TeamResponse> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map(teamMapper::toResponse)
                .toList();
    }


}