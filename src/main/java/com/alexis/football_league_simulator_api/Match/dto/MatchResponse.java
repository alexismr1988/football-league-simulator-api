package com.alexis.football_league_simulator_api.match.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchResponse {

    private Long id;
    private String leagueName;
    private int matchdayNumber;
    private String localTeamName;
    private String awayTeamName;
    private String score;
    private boolean played;

}
