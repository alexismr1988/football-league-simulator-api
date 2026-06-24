package com.alexis.football_league_simulator_api.match;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByLeagueId(Long leagueId);
    List<Match> findByHomeTeamIdOrAwayTeamId(Long homeTeamId, Long awayTeamId);
    List<Match> findByHomeTeamIdAndPlayedTrueOrAwayTeamIdAndPlayedTrue(Long homeTeamId, Long awayTeamId);
    List<Match> findByHomeTeamIdAndPlayedFalseOrAwayTeamIdAndPlayedFalse(Long homeTeamId, Long awayTeamId);
    List<Match> findByLeagueIdAndPlayedTrue(Long leagueId);
    List<Match> findByLeagueIdAndPlayedFalse(Long leagueId);
    boolean existsByLeagueId(Long leagueId);
    List<Match> findByLeagueIdAndMatchdayNumber(Long leagueId, int matchdayNumber);
}
