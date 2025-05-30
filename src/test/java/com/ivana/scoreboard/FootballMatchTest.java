package com.ivana.scoreboard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FootballMatchTest {

    @Test
    @DisplayName("Should construct a new FootballMatch with valid team names")
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

}