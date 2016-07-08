package com.tysci.cls.modles;

/**
 * Created by Administrator on 2016/7/5.
 */
public class UserInfoEntity {
    private int id;
    private double balance;
    private String phone;
    private boolean sex;
    private int quiz_total;
    private String nickname;
    private int quiz_go;
    private int user_id;
    private int quiz_lose;
    private int quiz_win;
    private String portrait;
    private float profits;
    private float profit_rate;

    public void setId(int id) {
        this.id = id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public void setQuiz_total(int quiz_total) {
        this.quiz_total = quiz_total;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setQuiz_go(int quiz_go) {
        this.quiz_go = quiz_go;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setQuiz_lose(int quiz_lose) {
        this.quiz_lose = quiz_lose;
    }

    public void setQuiz_win(int quiz_win) {
        this.quiz_win = quiz_win;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isSex() {
        return sex;
    }

    public int getQuiz_total() {
        return quiz_total;
    }

    public String getNickname() {
        return nickname;
    }

    public int getQuiz_go() {
        return quiz_go;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getQuiz_lose() {
        return quiz_lose;
    }

    public int getQuiz_win() {
        return quiz_win;
    }

    public String getPortrait() {
        return portrait;
    }

    public float getProfits() {
        return profits;
    }

    public void setProfits(float profits) {
        this.profits = profits;
    }

    public float getProfit_rate() {
        return profit_rate;
    }

    public void setProfit_rate(float profit_rate) {
        this.profit_rate = profit_rate;
    }
}
