package com.alexis.football_league_simulator_api.coach;

import java.util.List;

public enum CoachStyle {

    DEFENSIVE(List.of("541", "532", "451")),
    OFFENSIVE(List.of("433", "343", "352")),
    POSSESSION(List.of("442", "433", "451")),
    COUNTER_ATTACK(List.of("532", "352", "361"));

    private final List<String> formations;

    CoachStyle(List<String> formations) {
        this.formations = formations;
    }

    public List<String> getFormations() {
        return formations;
    }
}
