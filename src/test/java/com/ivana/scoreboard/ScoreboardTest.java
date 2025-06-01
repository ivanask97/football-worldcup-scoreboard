package com.ivana.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
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

        assertEquals("Can not update match that doesn't exist", exception.getMessage());
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

    @Test
    @DisplayName("Finish match method should remove match from scoreboard and teams from active teams successufully")
    void finishMatch_shouldRemoveMatchAndTeamsSuccessfully() {
        scoreboard.startMatch("Brazil", "Croatia");

        scoreboard.finishMatch("Brazil", "Croatia");

        assertFalse(scoreboard.getScoreboard().containsKey("brazil_vs_croatia"));
        assertFalse(scoreboard.getActiveTeams().contains("brazil"));
        assertFalse(scoreboard.getActiveTeams().contains("croatia"));
    }

    @Test
    @DisplayName("Finish match method should be case insensitive and trimmed")
    void finishMatch_shouldBeCaseInsensitiveAndTrimmed() {
        scoreboard.startMatch("  Brazil ", " Croatia ");

        scoreboard.finishMatch("brazil", "CROATIA");

        assertFalse(scoreboard.getScoreboard().containsKey("brazil_vs_croatia"));
        assertFalse(scoreboard.getActiveTeams().contains("brazil"));
        assertFalse(scoreboard.getActiveTeams().contains("croatia"));
    }

    @Test
    @DisplayName("Finish match method should throw exception if match does not exists")
    void finishMatch_shouldThrowExceptionIfMatchDoesNotExist() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                scoreboard.finishMatch("Argentina", "Germany")
        );

        assertEquals("Can not finish the match that is not ongoing", exception.getMessage());
    }

    @Test
    @DisplayName("Finish match method should only remove given match")
    void finishMatch_shouldOnlyRemoveGivenMatch() {
        scoreboard.startMatch("Brazil", "Croatia");
        scoreboard.startMatch("Spain", "France");

        scoreboard.finishMatch("Brazil", "Croatia");

        assertTrue(scoreboard.getScoreboard().containsKey("spain_vs_france"));
        assertFalse(scoreboard.getScoreboard().containsKey("brazil_vs_croatia"));
    }

    @Test
    @DisplayName("Get summary method should return matchess sorted by the total score")
    void getSummary_shouldReturnMatchesSortedByTotalScore() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 0, 5);

        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.updateScore("Spain", "Brazil", 10, 2);

        List<String> summary = scoreboard.getSummary();

        assertEquals(2, summary.size());
        assertEquals("Spain 10 - Brazil 2", summary.getFirst());
        assertEquals("Mexico 0 - Canada 5", summary.get(1));
    }

    @Test
    @DisplayName("Get summary method should give the most recently started mach if two matches have the same total result")
    void getSummary_shouldBreakTiesByMostRecentlyStartedMatch() {
        scoreboard.startMatch("Germany", "France");
        scoreboard.updateScore("Germany", "France", 2, 2);

        scoreboard.startMatch("Italy", "Portugal");
        scoreboard.updateScore("Italy", "Portugal", 1, 3);

        List<String> summary = scoreboard.getSummary();

        assertEquals("Italy 1 - Portugal 3", summary.getFirst());
        assertEquals("Germany 2 - France 2", summary.get(1));
    }

    @Test
    @DisplayName("Get summary method should return Empty list if there is no ongoing matches")
    void getSummary_shouldReturnEmptyListWhenNoMatches() {
        List<String> summary = scoreboard.getSummary();
        assertTrue(summary.isEmpty());
    }

    @Test
    @DisplayName("Get summary method should not show any match that already finished")
    void getSummary_shouldNotIncludeFinishedMatches() {
        scoreboard.startMatch("Argentina", "Chile");
        scoreboard.updateScore("Argentina", "Chile", 2, 2);

        scoreboard.finishMatch("Argentina", "Chile");

        List<String> summary = scoreboard.getSummary();

        assertTrue(summary.isEmpty());
    }

    @Test
    @DisplayName("Get summary method should treat names case insensitive and trimmed")
    void getSummary_shouldTreatTeamNamesCaseInsensitivelyAndTrimmed() {
        scoreboard.startMatch("  argentina", "Brazil  ");
        scoreboard.updateScore("ARGENTINA", "brazil", 1, 1);

        List<String> summary = scoreboard.getSummary();

        assertEquals(1, summary.size());
        assertEquals("argentina 1 - Brazil 1", summary.getFirst());
    }

    @Test
    @DisplayName("Get summary method should still show ongoing matchesk that have starting results")
    void getSummary_shouldIncludeMatchesWithZeroScore() {
        scoreboard.startMatch("USA", "Canada");
        scoreboard.updateScore("USA", "Canada", 0, 0);

        List<String> summary = scoreboard.getSummary();

        assertEquals(1, summary.size());
        assertEquals("USA 0 - Canada 0", summary.getFirst());
    }

    @Test
    @DisplayName("Get summary method should sort as many ongoing matches there are correctly")
    void getSummary_shouldSortMultipleMatchesCorrectly() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 0, 5);

        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.updateScore("Spain", "Brazil", 10, 2);

        scoreboard.startMatch("Germany", "France");
        scoreboard.updateScore("Germany", "France", 2, 2);

        scoreboard.startMatch("Italy", "Portugal");
        scoreboard.updateScore("Italy", "Portugal", 1, 3);

        List<String> summary = scoreboard.getSummary();

        assertEquals("Spain 10 - Brazil 2", summary.getFirst());
        assertEquals("Mexico 0 - Canada 5", summary.get(1));
        assertEquals("Italy 1 - Portugal 3", summary.get(2));
        assertEquals("Germany 2 - France 2", summary.get(3));
    }

    @Test
    @DisplayName("Get summary method should still keep track if matchess are updated regulary")
    void getSummary_shouldReflectUpdatedScores() {
        scoreboard.startMatch("Team A", "Team B");
        scoreboard.updateScore("Team A", "Team B", 1, 1);

        scoreboard.startMatch("Team C", "Team D");
        scoreboard.updateScore("Team C", "Team D", 2, 1);

        scoreboard.updateScore("Team A", "Team B", 3, 2);

        List<String> summary = scoreboard.getSummary();

        assertEquals("Team A 3 - Team B 2", summary.getFirst());
        assertEquals("Team C 2 - Team D 1", summary.get(1));
    }
}