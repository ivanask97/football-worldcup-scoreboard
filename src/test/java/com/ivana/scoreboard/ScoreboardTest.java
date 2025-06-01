package com.ivana.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardTest {
    private Scoreboard scoreboard;

    @BeforeEach
    void setUp(){
        scoreboard = new Scoreboard();
    }

    @Test
    @DisplayName("Start match method should add match successufully to scoreboard")
    void startMatch_shouldAddMatchSuccessfully(){
        scoreboard.startMatch("Brazil", "Croatia");
        assertEquals(1, scoreboard.getScoreboard().size());
        assertTrue(scoreboard.getScoreboard().containsKey("brazil_vs_croatia"));
    }

    @Test
    @DisplayName("Start match should throw an exception if team is already in the active match")
    void startMatch_shouldThrowExceptionIfTeamAlreadyInActiveMatch(){
        scoreboard.startMatch("Brazil", "Croatia");
        IllegalStateException exception = assertThrows(IllegalStateException.class,
        () -> {
            scoreboard.startMatch("Brazil", "Mexico");
        });
        assertEquals("This match can not be initialized since one of the teams is already in the game", exception.getMessage());
    }

    @Test
    @DisplayName("Start match method should throw illegal state exception if match already exists regardless of the case and spacing")
    void startMatch_shouldThrowExceptionIfMatchAlreadyExistsRegardlessOfCaseSpacing(){
        scoreboard.startMatch("Brazil", "Croatia");
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            scoreboard.startMatch("BraZil     ", "Croatia   ");
        } );
        assertEquals("This match is already in progress", exception.getMessage());
    }
}