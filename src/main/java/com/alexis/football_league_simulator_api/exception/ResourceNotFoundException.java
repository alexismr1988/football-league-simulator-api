package com.alexis.football_league_simulator_api.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String resourceName, Object id) {
        super(resourceName + " with ID " + id + " not found.");
    }
}
