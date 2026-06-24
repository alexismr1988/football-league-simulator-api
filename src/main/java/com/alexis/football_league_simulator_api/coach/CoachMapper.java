package com.alexis.football_league_simulator_api.coach;

import com.alexis.football_league_simulator_api.coach.dto.CoachResponse;
import com.alexis.football_league_simulator_api.coach.dto.CreateCoachRequest;
import com.alexis.football_league_simulator_api.coach.dto.UpdateCoachRequest;
import org.springframework.stereotype.Component;

@Component
public class CoachMapper {

    public CoachResponse toResponse(Coach coach){
        CoachResponse coachResponse = new CoachResponse();
        coachResponse.setId(coach.getId());
        coachResponse.setName(coach.getName());
        coachResponse.setCoachStyle(coach.getCoachStyle());
        if (coach.getTeam() != null) {
            coachResponse.setTeamId(coach.getTeam().getId());
            coachResponse.setTeamName(coach.getTeam().getName());
        }

        return coachResponse;
    }

    public Coach toEntity(CreateCoachRequest createCoachRequest){
        Coach coach = new Coach();
        coach.setName(createCoachRequest.getName());
        coach.setCoachStyle(createCoachRequest.getCoachStyle());

        return  coach;
    }

    public void updateEntity(UpdateCoachRequest updateCoachRequest, Coach coach) {
        coach.setName(updateCoachRequest.getName());
        coach.setCoachStyle(updateCoachRequest.getCoachStyle());
    }
}
