package com.ivana.scoreboard;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Scoreboard {
    private final LinkedHashMap<String, FootballMatch> scoreboard;
    private final HashSet<String> activeTeams;

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
    private boolean isTeamInActiveMatch(String teamName){
        String teamNameNormalize =  teamName.trim().toLowerCase();
        return this.activeTeams.contains(teamNameNormalize);
    }
    private String createMatchKey(String homeTeam, String awayTeam) {
        return homeTeam.trim().toLowerCase() + "_vs_" + awayTeam.trim().toLowerCase();
    }

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
    private void validateScore(int score){
        if(score < 0){
            throw new IllegalArgumentException("Score can not be a negative number");
        }
        if (score > 100) {
            throw new IllegalArgumentException("Score number seems unrealistic, check the score input");
        }

    }
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
