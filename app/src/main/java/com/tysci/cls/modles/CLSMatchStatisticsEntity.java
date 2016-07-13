package com.tysci.cls.modles;

/**
 * Created by Administrator on 2016/7/11.
 */
public class CLSMatchStatisticsEntity {

    /**
     * sortIndex : 3
     * id : 655
     * homeTeamValue : 7
     * awayTeamId : 371
     * awayTeamValue : 5
     * homeTeamId : 372
     * matchId : 3033
     * statisticKey : 射中门框以内
     */

    private int sortIndex;
    private int id;
    private String homeTeamValue;
    private int awayTeamId;
    private String awayTeamValue;
    private int homeTeamId;
    private int matchId;
    private String statisticKey;

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHomeTeamValue(String homeTeamValue) {
        this.homeTeamValue = homeTeamValue;
    }

    public void setAwayTeamId(int awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public void setAwayTeamValue(String awayTeamValue) {
        this.awayTeamValue = awayTeamValue;
    }

    public void setHomeTeamId(int homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public void setStatisticKey(String statisticKey) {
        this.statisticKey = statisticKey;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public int getId() {
        return id;
    }

    public String getHomeTeamValue() {
        return homeTeamValue;
    }

    public int getAwayTeamId() {
        return awayTeamId;
    }

    public String getAwayTeamValue() {
        return awayTeamValue;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }

    public int getMatchId() {
        return matchId;
    }

    public String getStatisticKey() {
        return statisticKey;
    }
}
