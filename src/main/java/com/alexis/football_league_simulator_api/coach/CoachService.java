package com.alexis.football_league_simulator_api.coach;

import com.alexis.football_league_simulator_api.coach.dto.CoachResponse;
import com.alexis.football_league_simulator_api.coach.dto.CreateCoachRequest;
import com.alexis.football_league_simulator_api.coach.dto.UpdateCoachRequest;
import com.alexis.football_league_simulator_api.exception.ResourceNotFoundException;
import com.alexis.football_league_simulator_api.team.Team;
import com.alexis.football_league_simulator_api.team.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CoachService {

    private final CoachRepository coachRepository;
    private final CoachMapper coachMapper;
    private final TeamRepository teamRepository;

    public CoachResponse saveCoach(CreateCoachRequest createCoachRequest){
        Coach coach = coachMapper.toEntity(createCoachRequest);

        Coach savedCoach = coachRepository.save(coach);

        return coachMapper.toResponse(savedCoach);
    }

    @Transactional
    public CoachResponse assignCoachToTeam(Long coachId, Long teamId) {
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new ResourceNotFoundException("Coach", coachId));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Team", teamId)
                );

        team.setCoach(coach);
        coach.setTeam(team);

        teamRepository.save(team);

        return coachMapper.toResponse(coach);
    }

    public CoachResponse getCoachByTeamId(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Team", teamId)
                );
        Coach coach = coachRepository.findByTeamId(teamId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Coach", teamId)
                );

        return  coachMapper.toResponse(coach);
    }

    public void unassignCoachFromTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Team", teamId)
                );

        Coach coach = team.getCoach();

        if (coach != null) {
            coach.setTeam(null);
            team.setCoach(null);
            teamRepository.save(team);
        }
    }

    public CoachResponse updateCoach(Long id, UpdateCoachRequest updateCoachRequest){
        Coach coach = coachRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coach", id));

        coachMapper.updateEntity(updateCoachRequest, coach);
        Coach updatedCoach = coachRepository.save(coach);
        return coachMapper.toResponse(updatedCoach);
    }

    public List<CoachResponse> getAllCoaches() {
        return coachRepository.findAll()
                .stream()
                .map(coachMapper::toResponse)
                .toList();
    }

}
