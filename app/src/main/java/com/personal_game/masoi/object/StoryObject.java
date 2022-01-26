package com.personal_game.masoi.object;

public class StoryObject {
    private String id;
    private String name;
    private String historyId;
    private String startTime;
    private String endTime;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public StoryObject(String id, String name, String historyId, String startTime, String endTime, String content) {
        this.id = id;
        this.name = name;
        this.historyId = historyId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.content = content;
    }
}
