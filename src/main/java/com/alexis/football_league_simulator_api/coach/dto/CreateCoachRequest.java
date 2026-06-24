package com.alexis.football_league_simulator_api.coach.dto;

import com.alexis.football_league_simulator_api.coach.CoachStyle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCoachRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Coach style cannot be null")
    private CoachStyle coachStyle;
}
