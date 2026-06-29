package com.alexis.football_league_simulator_api.league;

import com.alexis.football_league_simulator_api.match.Match;
import com.alexis.football_league_simulator_api.team.Team;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "league")
    private List<Team> teams = new ArrayList<>();

    public void setTeams(List<Team> teams) {
        if (teams.size() < 4 || teams.size() > 22) {
            throw new IllegalArgumentException(
                    "A league must have between 4 and 22 teams"
            );
        }

        this.teams = teams;
    }

    public void add(Team team) {
        if (teams.size() >= 22) {
            throw new IllegalArgumentException(
                    "A league cannot have more than 22 teams"
            );
        }

        teams.add(team);
    }
}
