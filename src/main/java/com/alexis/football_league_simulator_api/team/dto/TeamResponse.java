package com.alexis.football_league_simulator_api.team.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponse {

    private Long id;
    private String name;
    private BigDecimal budget;
}