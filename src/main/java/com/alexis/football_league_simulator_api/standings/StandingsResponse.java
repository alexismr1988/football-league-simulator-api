package com.alexis.football_league_simulator_api.standings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandingsResponse {

    private int position;
    private String teamName;
    private int points;
    private int goalsFor;
    private int goalsAgainst;
    private int goalDifference;
    private int wins;
    private int losses;
    private int draws;

}
