package com.ivana.scoreboard;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Scoreboard {
    /**
     * Map storing active matches, preserving insertion order for tie-breaking.
     * Key format: "hometeam_vs_awayteam" (normalized to lowercase)
     * Set of team names currently participating in active matches.
     * Team names are normalized to lowercase for case-insensitive comparison.
     */
    private final LinkedHashMap<String, FootballMatch> scoreboard;
    private final HashSet<String> activeTeams;

    /**
     * Constructs a new empty Scoreboard with no active matches.
     */
    public Scoreboard() {
        this.scoreboard = new LinkedHashMap<>();
        this.activeTeams = new HashSet<>();
    }

    public Set<String> getActiveTeams() {
        return Collections.unmodifiableSet(new HashSet<>(activeTeams));
    }

    public Map<String, FootballMatch> getScoreboard() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(scoreboard));
    }
    /**
     * Checks if a team is currently participating in an active match.
     * Comparison is case-insensitive and ignores whitespace.
     *
     * @param teamName the name of the team to check
     * @return true if the team is currently in an active match, if not it returns false
     */
    private boolean isTeamInActiveMatch(String teamName){
        String teamNameNormalize =  teamName.trim().toLowerCase();
        return this.activeTeams.contains(teamNameNormalize);
    }

    /**
     * Creates a unique match identifier key from home and away team names. Names are trimmed and lower cased
     *
     * @param homeTeam the home team name
     * @param awayTeam the away team name
     * @return formatted match key in the format "hometeam_vs_awayteam"
     */
    private String createMatchKey(String homeTeam, String awayTeam) {
        return homeTeam.trim().toLowerCase() + "_vs_" + awayTeam.trim().toLowerCase();
    }


    /**
     * Starts a new football match between the specified teams.
     * Both teams must not be currently participating in any other active matches.
     */
    public void startMatch(String homeTeam, String awayTeam){
        String matchKey = createMatchKey(homeTeam,awayTeam);
        if (scoreboard.containsKey(matchKey)){
            throw new IllegalStateException("This match is already in progress");
        }
        if (isTeamInActiveMatch(homeTeam) || isTeamInActiveMatch(awayTeam)){
            throw new IllegalStateException("This match can not be initialized since one of the teams is already in the game");
        }
        FootballMatch footballMatch = new FootballMatch(homeTeam,awayTeam);
        scoreboard.put(matchKey,footballMatch);
        activeTeams.add(homeTeam.trim().toLowerCase());
        activeTeams.add(awayTeam.trim().toLowerCase());
    }

    /**
     * Validates that a score value is within acceptable range.
     * Scores must be non-negative and not exceed 100 (since 100 to score in one ongoing match is unrealisitic).
     */
    private void validateScore(int score){
        if(score < 0){
            throw new IllegalArgumentException("Score can not be a negative number");
        }
        if (score > 100) {
            throw new IllegalArgumentException("Score number seems unrealistic, check the score input");
        }

    }

    /**
     * Updates the score for an existing ongoing match.
     */
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore){
        String matchKey = createMatchKey(homeTeam,awayTeam);
        if (scoreboard.containsKey(matchKey)){
            FootballMatch match = scoreboard.get(matchKey);
            validateScore(homeScore);
            validateScore(awayScore);
            match.setHomeScore(homeScore);
            match.setAwayScore(awayScore);
        }else{
            throw new IllegalStateException("Can not update match that doesn't exist");
        }
    }

    /**
     * Finishes ongoing match and removes it from the scoreboard.
     */
    public void finishMatch(String homeTeam, String awayTeam){
        String matchKey = createMatchKey(homeTeam,awayTeam);
        if (scoreboard.containsKey(matchKey)){
            scoreboard.remove(matchKey);
            activeTeams.remove(homeTeam.trim().toLowerCase());
            activeTeams.remove(awayTeam.trim().toLowerCase());
        }else{
            throw new IllegalStateException("Can not finish the match that is not ongoing");
        }
    }


    /**
     * Generates a summary of all active matches, sorted first by the highest total score,
     * but if there is a tie than the match tthat started the most recently is going to be shown first
     */
    public List<String> getSummary() {
        AtomicInteger insertionOrder = new AtomicInteger(0);

        return scoreboard.values().stream()
                .map(footballMatch -> new Object() {
                    final FootballMatch match = footballMatch;
                    final int order = insertionOrder.getAndIncrement();
                    final int totalScore = footballMatch.getTotalScore();
                })
                .sorted((obj1, obj2) -> {
                    int scoreComp = Integer.compare(obj2.totalScore, obj1.totalScore);
                    if (scoreComp != 0) {
                        return scoreComp;
                    }
                    return Integer.compare(obj2.order, obj1.order);
                })
                .map(obj -> {
                    FootballMatch m = obj.match;
                    return m.getHomeTeam() + " " + m.getHomeScore() + " - " +
                            m.getAwayTeam() + " " + m.getAwayScore();
                })
                .collect(Collectors.toList());
    }
}
