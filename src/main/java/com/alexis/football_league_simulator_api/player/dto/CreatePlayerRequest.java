package com.alexis.football_league_simulator_api.player.dto;

import com.alexis.football_league_simulator_api.player.Position;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlayerRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Min(value = 16, message = "Players can not be younger than 16.")
    @Max(value = 42, message = "Players can not be older than 42.")
    private int age;

    @Min(value = 1, message = "Overall can not be lower than 1.")
    @Max(value = 100, message = "Overall can not be higher than 100.")
    private int overall;

    @NotNull(message = "Position cannot be null")
    private Position position;
}
