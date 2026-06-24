package com.alexis.football_league_simulator_api.coach;

import com.alexis.football_league_simulator_api.team.Team;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CoachStyle coachStyle;

    @OneToOne(mappedBy = "coach")
    private Team team;
}
