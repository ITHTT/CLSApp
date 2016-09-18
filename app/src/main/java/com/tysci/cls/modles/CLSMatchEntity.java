package com.tysci.cls.modles;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/7.
 */
public class CLSMatchEntity implements Parcelable {

    /**
     * status : 100
     * redTip : 1
     * source_id : 8969995
     * homeTeamFlag : http://192.168.10.21:8080/images/logo/yongchang.png
     * awayTeamLineup :
     * matchTime : 15:30
     * stage : 1
     * homeTeamScore : 0
     * id : 3142
     * matchDateTransWeek : 2016-03-06
     * homeTeamLineup :
     * matchDate : 3月6日
     * awayTeamId : 375
     * homeTeamId : 369
     * forOrderDate : 0306
     * awayTeamName : 辽宁宏运
     * homeTeamName : 石家庄永昌
     * ballqMatchId : 101418
     * awayTeamFlag : http://192.168.10.21:8080/images/logo/hongyun.png
     * awayTeamScore : 1
     */

    private int status;
    private int redTip;
    private int source_id;
    private String homeTeamFlag;
    private String awayTeamLineup;
    private String matchTime;
    private int stage;
    private int homeTeamScore;
    private int id;
    private String matchDateTransWeek;
    private String homeTeamLineup;
    private String matchDate;
    private int awayTeamId;
    private int homeTeamId;
    private String forOrderDate;
    private String awayTeamName;
    private String homeTeamName;
    private int ballqMatchId;
    private String awayTeamFlag;
    private int awayTeamScore;
    private String groupName;
    private String matchDateWeek;
    private int chatRoomStatus;
    private String chatRoomId;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setRedTip(int redTip) {
        this.redTip = redTip;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public void setHomeTeamFlag(String homeTeamFlag) {
        this.homeTeamFlag = homeTeamFlag;
    }

    public void setAwayTeamLineup(String awayTeamLineup) {
        this.awayTeamLineup = awayTeamLineup;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public void setHomeTeamScore(int homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMatchDateTransWeek(String matchDateTransWeek) {
        this.matchDateTransWeek = matchDateTransWeek;
    }

    public void setHomeTeamLineup(String homeTeamLineup) {
        this.homeTeamLineup = homeTeamLineup;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public void setAwayTeamId(int awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public void setHomeTeamId(int homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public void setForOrderDate(String forOrderDate) {
        this.forOrderDate = forOrderDate;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public void setBallqMatchId(int ballqMatchId) {
        this.ballqMatchId = ballqMatchId;
    }

    public void setAwayTeamFlag(String awayTeamFlag) {
        this.awayTeamFlag = awayTeamFlag;
    }

    public void setAwayTeamScore(int awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public int getStatus() {
        return status;
    }

    public int getRedTip() {
        return redTip;
    }

    public int getSource_id() {
        return source_id;
    }

    public String getHomeTeamFlag() {
        return homeTeamFlag;
    }

    public String getAwayTeamLineup() {
        return awayTeamLineup;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public int getStage() {
        return stage;
    }

    public int getHomeTeamScore() {
        return homeTeamScore;
    }

    public int getId() {
        return id;
    }

    public String getMatchDateTransWeek() {
        return matchDateTransWeek;
    }

    public String getHomeTeamLineup() {
        return homeTeamLineup;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public int getAwayTeamId() {
        return awayTeamId;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }

    public String getForOrderDate() {
        return forOrderDate;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public int getBallqMatchId() {
        return ballqMatchId;
    }

    public String getAwayTeamFlag() {
        return awayTeamFlag;
    }

    public int getAwayTeamScore() {
        return awayTeamScore;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMatchDateWeek() {
        return matchDateWeek;
    }

    public void setMatchDateWeek(String matchDateWeek) {
        this.matchDateWeek = matchDateWeek;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public int getChatRoomStatus() {
        return chatRoomStatus;
    }

    public void setChatRoomStatus(int chatRoomStatus) {
        this.chatRoomStatus = chatRoomStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeInt(this.redTip);
        dest.writeInt(this.source_id);
        dest.writeString(this.homeTeamFlag);
        dest.writeString(this.awayTeamLineup);
        dest.writeString(this.matchTime);
        dest.writeInt(this.stage);
        dest.writeInt(this.homeTeamScore);
        dest.writeInt(this.id);
        dest.writeString(this.matchDateTransWeek);
        dest.writeString(this.homeTeamLineup);
        dest.writeString(this.matchDate);
        dest.writeInt(this.awayTeamId);
        dest.writeInt(this.homeTeamId);
        dest.writeString(this.forOrderDate);
        dest.writeString(this.awayTeamName);
        dest.writeString(this.homeTeamName);
        dest.writeInt(this.ballqMatchId);
        dest.writeString(this.awayTeamFlag);
        dest.writeInt(this.awayTeamScore);
        dest.writeString(this.groupName);
        dest.writeString(this.matchDateWeek);
        dest.writeInt(this.chatRoomStatus);
        dest.writeString(this.chatRoomId);
    }

    public CLSMatchEntity() {
    }

    private CLSMatchEntity(Parcel in) {
        this.status = in.readInt();
        this.redTip = in.readInt();
        this.source_id = in.readInt();
        this.homeTeamFlag = in.readString();
        this.awayTeamLineup = in.readString();
        this.matchTime = in.readString();
        this.stage = in.readInt();
        this.homeTeamScore = in.readInt();
        this.id = in.readInt();
        this.matchDateTransWeek = in.readString();
        this.homeTeamLineup = in.readString();
        this.matchDate = in.readString();
        this.awayTeamId = in.readInt();
        this.homeTeamId = in.readInt();
        this.forOrderDate = in.readString();
        this.awayTeamName = in.readString();
        this.homeTeamName = in.readString();
        this.ballqMatchId = in.readInt();
        this.awayTeamFlag = in.readString();
        this.awayTeamScore = in.readInt();
        this.groupName = in.readString();
        this.matchDateWeek = in.readString();
        this.chatRoomStatus=in.readInt();
        this.chatRoomId=in.readString();
    }

    public static final Parcelable.Creator<CLSMatchEntity> CREATOR = new Parcelable.Creator<CLSMatchEntity>() {
        public CLSMatchEntity createFromParcel(Parcel source) {
            return new CLSMatchEntity(source);
        }

        public CLSMatchEntity[] newArray(int size) {
            return new CLSMatchEntity[size];
        }
    };
}
