# Football League Simulator API

## Description
Football League Simulator API is a backend application developed with Spring Boot that allows users to manage football teams, players, coaches and leagues, generate round-robin schedules, simulate matchdays and calculate league standings.

The project is a progressive migration of an original desktop application built with Java Swing, JDBC and MySQL/MariaDB. The main goal of the migration is not to copy the previous code directly, but to preserve the main business logic while redesigning the application as a REST API using Spring Boot, Spring Data JPA, DTOs, services, repositories and controllers.

This first functional version includes the core features required to create and simulate a complete football league.

## Current features
- Teams
- Players
- Coaches
- Leagues
- Schedule generation
- Matchday simulation
- Standings

## Technologies
Java 21, Spring Boot, JPA, MySQL, Maven, Validation, Lombok.

## Project status
First functional backend version.

## How to run

### Requirements

* Java 21
* Maven
* MySQL or MariaDB
* IntelliJ IDEA or another Java IDE

### Environment variables

Configure the following environment variables before running the application:

```text
DB_URL=jdbc:mysql://localhost:3306/football_league_simulator
DB_USERNAME=root
DB_PASSWORD=your_password
```

Create the database before starting the application:

```sql
CREATE DATABASE football_league_simulator;
```

### Run the application

Using Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

## Main endpoints

### Leagues

```http
POST   /leagues
GET    /leagues
GET    /leagues/{leagueId}
GET    /leagues/{leagueId}/teams
PUT    /leagues/{leagueId}
DELETE /leagues/{leagueId}
```

### Schedule and matches

```http
POST /leagues/{leagueId}/schedule

GET /leagues/{leagueId}/matches
GET /leagues/{leagueId}/matches/played
GET /leagues/{leagueId}/matches/pending
```

### Players

```http
POST   /teams/{teamId}/players
POST   /teams/{teamId}/players/batch
GET    /teams/{teamId}/players
GET    /players/{playerId}
PUT    /players/{playerId}
DELETE /players/{playerId}
```

### Simulation

```http
POST /leagues/{leagueId}/matchdays/{matchdayNumber}/simulate
```

### Standings

```http
GET /leagues/{leagueId}/standings
```


## Future improvements
Security, tests, AI-generated match reports, frontend.