package com.tysci.cls.modles;

/**
 * Created by Administrator on 2016/7/8.
 */
public class CLSMatchLineupEntity {

    /**
     * position : D
     * id : 15252
     * playerNo : 14
     * playerName : Ju Young Kim
     * playerId : 1089
     * firstFlag : 1
     * matchId : 3146
     * teamId : 383
     */

    private String position;
    private int id;
    private int playerNo;
    private String playerName;
    private int playerId;
    private int firstFlag;
    private int matchId;
    private int teamId;
    private int type;

    public void setPosition(String position) {
        this.position = position;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlayerNo(int playerNo) {
        this.playerNo = playerNo;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setFirstFlag(int firstFlag) {
        this.firstFlag = firstFlag;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }

    public int getPlayerNo() {
        return playerNo;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getFirstFlag() {
        return firstFlag;
    }

    public int getMatchId() {
        return matchId;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
