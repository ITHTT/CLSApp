package com.tysci.cls.modles;

/**
 * Created by Administrator on 2016/7/11.
 */
public class CLSMatchEventEntity {

    /**
     * id : 935
     * time : 90
     * playerName : 吉利奥蒂
     * source_id : 232641221
     * matchId : 3033
     * eventImage : http://192.168.10.21:8080/images/event/5.png
     * type : 5
     * teamId : 371
     */

    private int id;
    private int time;
    private String playerName;
    private int source_id;
    private int matchId;
    private String eventImage;
    private int type;
    private int teamId;

    private String homePlayerName;
    private String homeEventImage;
    private String awayPlayerName;
    private String awayEventImage;

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getId() {
        return id;
    }

    public int getTime() {
        return time;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getSource_id() {
        return source_id;
    }

    public int getMatchId() {
        return matchId;
    }

    public String getEventImage() {
        return eventImage;
    }

    public int getType() {
        return type;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getAwayEventImage() {
        return awayEventImage;
    }

    public void setAwayEventImage(String awayEventImage) {
        this.awayEventImage = awayEventImage;
    }

    public String getAwayPlayerName() {
        return awayPlayerName;
    }

    public void setAwayPlayerName(String awayPlayerName) {
        this.awayPlayerName = awayPlayerName;
    }

    public String getHomeEventImage() {
        return homeEventImage;
    }

    public void setHomeEventImage(String homeEventImage) {
        this.homeEventImage = homeEventImage;
    }

    public String getHomePlayerName() {
        return homePlayerName;
    }

    public void setHomePlayerName(String homePlayerName) {
        this.homePlayerName = homePlayerName;
    }
}
