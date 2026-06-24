package com.alexis.football_league_simulator_api.coach.dto;

import com.alexis.football_league_simulator_api.coach.CoachStyle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoachResponse {

    private Long id;
    private String name;
    private CoachStyle coachStyle;
    private Long teamId;
    private String teamName;
}
