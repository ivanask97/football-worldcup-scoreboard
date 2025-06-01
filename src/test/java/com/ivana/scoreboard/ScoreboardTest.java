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
    @Test
    @DisplayName("Update score method should update score successufully when match exists and scores are valid")
    void updateScore_shouldUpdateScoreSuccessfully_whenMatchExistsAndScoresValid() {
        scoreboard.startMatch("Brazil", "Croatia");
        scoreboard.updateScore("Brazil", "Croatia", 2, 1);

        String key = "brazil_vs_croatia";
        FootballMatch match = scoreboard.getScoreboard().get(key);
        assertNotNull(match);
        assertEquals(2, match.getHomeScore());
        assertEquals(1, match.getAwayScore());
    }

    @Test
    @DisplayName("Update score method should throw the exception when match does not exists")
    void updateScore_shouldThrowException_whenMatchDoesNotExist() {
        Exception exception = assertThrows(IllegalStateException.class, () ->
                scoreboard.updateScore("Germany", "France", 3, 2));

        assertEquals("This match does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("Update score method should throw the exception when home score is negative number")
    void updateScore_shouldThrowException_whenHomeScoreIsNegative() {
        scoreboard.startMatch("Spain", "Italy");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                scoreboard.updateScore("Spain", "Italy", -1, 2));

        assertEquals("Score can not be a negative number", exception.getMessage());
    }

    @Test
    @DisplayName("Update score method should throw the exception when away score is negative number")
    void updateScore_shouldThrowException_whenAwayScoreIsNegative() {
        scoreboard.startMatch("Argentina", "Netherlands");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                scoreboard.updateScore("Argentina", "Netherlands", 3, -2));

        assertEquals("Score can not be a negative number", exception.getMessage());
    }

    @Test
    @DisplayName("Update score method should throw the exception when score is negative unrealisticaly high number")
    void updateScore_shouldThrowException_whenScoreIsUnrealisticallyHigh() {
        scoreboard.startMatch("England", "USA");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                scoreboard.updateScore("England", "USA", 101, 1));

        assertEquals("Score number seems unrealistic, check the score input", exception.getMessage());
    }

    @Test
    @DisplayName("Update score method should be case insensitive and trimmed")
    void updateScore_shouldBeCaseInsensitiveAndTrimmed() {
        scoreboard.startMatch("  Japan", "SOUTH Korea  ");
        scoreboard.updateScore("japan", "south korea", 1, 1);

        String key = "japan_vs_south korea";
        FootballMatch match = scoreboard.getScoreboard().get(key);
        assertEquals(1, match.getHomeScore());
        assertEquals(1, match.getAwayScore());
    }

    @Test
    @DisplayName("Update score method should allow score of 0 and 100")
    void updateScore_shouldAllowScoreOfZeroAndHundred() {
        scoreboard.startMatch("Poland", "Ukraine");
        scoreboard.updateScore("Poland", "Ukraine", 0, 100);

        FootballMatch match = scoreboard.getScoreboard().get("poland_vs_ukraine");
        assertEquals(0, match.getHomeScore());
        assertEquals(100, match.getAwayScore());
    }

}