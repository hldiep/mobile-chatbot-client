package com.example.mychatbot.api;

public class HistoryItem {
    private String user;
    private String assistant;

    public HistoryItem(String user, String assistant) {
        this.user = user;
        this.assistant = assistant;
    }

    public String getUser() {
        return user;
    }

    public String getAssistant() {
        return assistant;
    }
}
