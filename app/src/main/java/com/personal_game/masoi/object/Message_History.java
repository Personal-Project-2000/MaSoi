package com.personal_game.masoi.object;

import java.util.List;

public class Message_History {
    private int status1;
    private String notification1;

    public int getStatus1() {
        return status1;
    }

    public void setStatus1(int status1) {
        this.status1 = status1;
    }

    public String getNotification1() {
        return notification1;
    }

    public void setNotification1(String notification1) {
        this.notification1 = notification1;
    }

    public List<HistoryObject> getHistoryLists1() {
        return historyLists1;
    }

    public void setHistoryLists1(List<HistoryObject> historyLists1) {
        this.historyLists1 = historyLists1;
    }

    public Message_History(int status1, String notification1, List<HistoryObject> historyLists1) {
        this.status1 = status1;
        this.notification1 = notification1;
        this.historyLists1 = historyLists1;
    }

    private List<HistoryObject> historyLists1;
}
