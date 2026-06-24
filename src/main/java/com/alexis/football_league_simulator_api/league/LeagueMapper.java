package com.alexis.football_league_simulator_api.league;

import com.alexis.football_league_simulator_api.league.dto.CreateLeagueRequest;
import com.alexis.football_league_simulator_api.league.dto.LeagueResponse;
import com.alexis.football_league_simulator_api.league.dto.UpdateLeagueRequest;
import com.alexis.football_league_simulator_api.team.Team;
import com.alexis.football_league_simulator_api.team.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LeagueMapper {

    private final TeamMapper teamMapper;

    public LeagueResponse toResponse(League league){
        LeagueResponse leagueResponse = new  LeagueResponse();
        leagueResponse.setId(league.getId());
        leagueResponse.setName(league.getName());
        leagueResponse.setTeamCount(league.getTeams().size());
        leagueResponse.setTeams(league.getTeams().stream()
                .map(teamMapper::toResponse)
                .toList());

        return leagueResponse;
    }

    public League toEntity(CreateLeagueRequest createLeagueRequest , List<Team> teams){
        League league = new League();
        league.setName(createLeagueRequest.getName());
        league.setTeams(teams);

        return league;

    }

    public void updateEntity(UpdateLeagueRequest updateLeagueRequest, League league) {
        league.setName(updateLeagueRequest.getName());
    }
}
