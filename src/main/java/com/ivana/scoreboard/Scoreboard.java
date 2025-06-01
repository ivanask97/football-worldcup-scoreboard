package com.ivana.scoreboard;

import java.util.HashSet;
import java.util.LinkedHashMap;

public class Scoreboard {
    private final LinkedHashMap<String, FootballMatch> scoreboard;
    private final HashSet<String> activeTeams;

    public Scoreboard() {
        this.scoreboard = new LinkedHashMap<>();
        this.activeTeams = new HashSet<>();
    }

    public LinkedHashMap<String, FootballMatch> getScoreboard() {
        return scoreboard;
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
}
