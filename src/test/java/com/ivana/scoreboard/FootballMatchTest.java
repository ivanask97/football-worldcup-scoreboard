package com.ivana.scoreboard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FootballMatchTest {

    @Test
    @DisplayName("Should construct a new FootballMatch with valid team names and initial state")
    void shouldConstructFootballMatchWithValidTeamNames(){
        String homeTeam = "Brazil";
        String awayTeam = "Croatia";
        FootballMatch match = new FootballMatch(homeTeam, awayTeam);
        assertEquals(homeTeam,match.getHomeTeam());
        assertEquals(awayTeam,match.getAwayTeam());
        assertEquals(0,match.getHomeScore());
        assertEquals(0,match.getAwayScore());
        assertTrue(match.getStartTime()>0);
    }

    @Test
    @DisplayName("Should validate start time")
    void shouldSetStartTimeToCurrentTime(){
        long beforeCreationTime = System.currentTimeMillis();

        FootballMatch match = new FootballMatch("Brazil", "Croatia");

        long afterCreationTime = System.currentTimeMillis();

        assertTrue(match.getStartTime()>=beforeCreationTime && match.getStartTime()<=afterCreationTime);
    }

    @Test
    @DisplayName("Should throw exception when home team is null")
    void shouldThrowIllegalArgumentExceptionWhenHomeTeamIsNull(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->{
            new FootballMatch(null, "Croatia");
        });

        assertEquals("Team name can not be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when away team is null")
    void shouldThrowIllegalArgumentExceptionWhenAwayTeamIsNull(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->{
            new FootballMatch("Brazil", null);
        });

        assertEquals("Team name can not be null or empty", exception.getMessage());
    }
    @Test
    @DisplayName("Should throw exception when home team is empty")
    void shouldThrowIllegalArgumentExceptionWhenHomeTeamIsEmpty(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new FootballMatch("","Croatia");
        });
        assertEquals("Team name can not be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when away team is empty")
    void shouldThrowIllegalArgumentExceptionWhenAwayTeamIsEmpty(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new FootballMatch("Brazil","");
        });
        assertEquals("Team name can not be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when home team is only whitespaces")
    void shouldThrowIllegalArgumentExceptionWhenHomeTeamIsOnlyWhitespace() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new FootballMatch("    ","Croatia");
        });
        assertEquals("Team name can not be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when away team is only whitespaces")
    void shouldThrowIllegalArgumentExceptionWhenAwayTeamIsOnlyWhitespace() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new FootballMatch("Brazil","   ");
        });
        assertEquals("Team name can not be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when team names are the same")
    void shouldThrowExceptionWhenTeamNamesAreTheSame(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new FootballMatch("Brazil", "Brazil");
        });
        assertEquals("Team names can not be the same", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when team names are the same (Case insensitive)")
    void shouldThrowExceptionWhenTeamNamesAreTheSameCaseInsensitive(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new FootballMatch("BrAziL", "bRaZil");
        });
        assertEquals("Team names can not be the same", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when team names are the same after trimming")
    void shouldThrowExceptionWhenTeamNamesAreTheSameAfterTrimming(){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new FootballMatch("BrAzil", "BraZil   ");
        });
        assertEquals("Team names can not be the same", exception.getMessage());
    }

    @Test
    void shouldHandleVeryLongTeamNames() {
        String longName = "A".repeat(1000);
        FootballMatch match = new FootballMatch(longName, "Croatia");
        assertEquals(longName, match.getHomeTeam());
    }
}