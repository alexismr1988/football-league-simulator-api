package com.alexis.football_league_simulator_api.league.dto;
import com.alexis.football_league_simulator_api.team.dto.TeamResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeagueResponse {

    private Long id;
    private String name;
    private int teamCount;
    private List<TeamResponse> teams;
}
