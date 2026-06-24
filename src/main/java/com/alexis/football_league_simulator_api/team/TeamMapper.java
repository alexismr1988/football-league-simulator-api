package com.alexis.football_league_simulator_api.team;

import com.alexis.football_league_simulator_api.team.dto.CreateTeamRequest;
import com.alexis.football_league_simulator_api.team.dto.TeamResponse;
import com.alexis.football_league_simulator_api.team.dto.UpdateTeamRequest;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    public TeamResponse toResponse(Team team) {
        TeamResponse teamResponse = new TeamResponse();
        teamResponse.setId(team.getId());
        teamResponse.setName(team.getName());
        teamResponse.setBudget(team.getBudget());

        return teamResponse;
    }

    public Team toEntity(CreateTeamRequest createTeamRequest) {
        Team team = new Team();
        team.setName(createTeamRequest.getName());
        team.setBudget(createTeamRequest.getBudget());

        return team;
    }

    public void updateEntity(UpdateTeamRequest updateTeamRequest, Team team) {
        team.setName(updateTeamRequest.getName());
        team.setBudget(updateTeamRequest.getBudget());
    }

}
