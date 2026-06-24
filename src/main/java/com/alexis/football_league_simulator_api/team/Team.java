package com.alexis.football_league_simulator_api.team;

import com.alexis.football_league_simulator_api.coach.Coach;
import com.alexis.football_league_simulator_api.league.League;
import com.alexis.football_league_simulator_api.lineup.Lineup;
import com.alexis.football_league_simulator_api.player.Player;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal budget;

    @OneToMany(
            mappedBy = "team",
            cascade = CascadeType.ALL
    )
    private List<Player> roster = new ArrayList<>();

    @Transient
    private Lineup lineup;

    @OneToOne
    @JoinColumn(name = "coach_id", unique = true)
    private Coach coach;

    @ManyToOne
    @JoinColumn(name = "league_id")
    private League league;

}
