package com.personal_game.masoi.object;

import java.io.Serializable;

public class HistoryObject implements Serializable {
    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public int getSl() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public HistoryObject(String historyId, int sl, String startTime, String time, boolean win) {
        this.historyId = historyId;
        this.sl = sl;
        this.startTime = startTime;
        this.time = time;
        this.win = win;
    }

    private String historyId;
    private int sl;
    private String startTime;
    private String time;
    private boolean win;
}
