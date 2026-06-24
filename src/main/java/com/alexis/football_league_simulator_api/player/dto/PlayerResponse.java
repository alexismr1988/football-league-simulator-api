package com.alexis.football_league_simulator_api.player.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.alexis.football_league_simulator_api.player.Position;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerResponse {

    private Long id;
    private String name;
    private int age;
    private int overall;
    private Long teamId;
    private String teamName;
    private Position position;
}
