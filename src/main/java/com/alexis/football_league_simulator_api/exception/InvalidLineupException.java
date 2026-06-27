package com.alexis.football_league_simulator_api.exception;

public class InvalidLineupException extends RuntimeException{

    public InvalidLineupException(String message) {
        super(message);
    }
}
