package com.personal_game.masoi.object;

import java.util.List;

public class Message_Story {
    private int status1;
    private String notification1;
    private List<PlayerObject> players1;
    private List<StoryObject> stories1;

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

    public List<PlayerObject> getPlayers1() {
        return players1;
    }

    public void setPlayers1(List<PlayerObject> players1) {
        this.players1 = players1;
    }

    public List<StoryObject> getStories1() {
        return stories1;
    }

    public void setStories1(List<StoryObject> stories1) {
        this.stories1 = stories1;
    }

    public Message_Story(int status1, String notification1, List<PlayerObject> players1, List<StoryObject> stories1) {
        this.status1 = status1;
        this.notification1 = notification1;
        this.players1 = players1;
        this.stories1 = stories1;
    }
}
