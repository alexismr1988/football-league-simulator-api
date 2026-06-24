package com.alexis.football_league_simulator_api.match;

import com.alexis.football_league_simulator_api.match.dto.MatchResponse;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {


    public MatchResponse toResponse(Match match) {
        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setId(match.getId());
        matchResponse.setLeagueName(match.getLeague().getName());
        matchResponse.setMatchdayNumber(match.getMatchdayNumber());
        matchResponse.setLocalTeamName(match.getHomeTeam().getName());
        matchResponse.setAwayTeamName(match.getAwayTeam().getName());
        matchResponse.setScore(
                match.isPlayed()
                        ? match.getHomeGoals() + " - " + match.getAwayGoals()
                        : null
        );
        matchResponse.setPlayed(match.isPlayed());

        return matchResponse;

    }
}
