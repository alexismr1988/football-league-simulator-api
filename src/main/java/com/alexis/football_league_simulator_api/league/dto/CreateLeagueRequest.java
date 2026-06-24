package com.alexis.football_league_simulator_api.league.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLeagueRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Team IDs cannot be null")
    @Size(min = 4, max = 22, message = "League size must be between 4 and 22 teams.")
    private List<Long> teamIds;
}
