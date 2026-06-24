package com.alexis.football_league_simulator_api.team.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTeamRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @PositiveOrZero(message = "Budget must be zero or positive")
    private BigDecimal budget;
}