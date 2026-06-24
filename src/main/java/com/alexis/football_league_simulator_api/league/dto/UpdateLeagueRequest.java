package com.alexis.football_league_simulator_api.league.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLeagueRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

}
