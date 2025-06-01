package com.ivana.scoreboard;

/**
 * Represents a football match between two teams.
 * Tracks team names, scores, and start time.
 */
public class FootballMatch implements Comparable<FootballMatch> {
    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;

    private String normalizeTeamName(String name) {
        return name == null ? null : name.trim();
    }

    private void validateTeam(String teamName) {
        if(teamName==null || teamName.isEmpty())
            throw new IllegalArgumentException("Team name can not be null or empty");
    }

    private void isSameTeam(String homeTeam, String awayTeam){
        if(homeTeam.equalsIgnoreCase(awayTeam))
            throw new IllegalArgumentException("Team names can not be the same");
    }

    /**
     * Creates a new FootballMatch with given teams, sets their initial scores to 0 and sets start time.
     *
     * @param homeTeam Name of the home team (non-null, non-empty, trimmed)
     * @param awayTeam Name of the away team (non-null, non-empty, trimmed, different from home team)
     * @throws IllegalArgumentException if validation fails
     */

    public FootballMatch(String homeTeam, String awayTeam) {
        homeTeam = normalizeTeamName(homeTeam);
        awayTeam = normalizeTeamName(awayTeam);

        validateTeam(homeTeam);
        validateTeam(awayTeam);
        isSameTeam(homeTeam, awayTeam);

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.awayScore = 0;
        this.homeScore = 0;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    private int getTotalScore(){
        return this.homeScore + this.awayScore;
    }

    @Override
    public int compareTo(FootballMatch other) {
        return Integer.compare(other.getTotalScore(), this.getTotalScore());
    }
}