package com.example.mychatbot.api;

import java.util.List;

public class ChatRequest {
    private String query;
    private List<HistoryItem> history;

    public ChatRequest(String query, List<HistoryItem> history) {
        this.query = query;
        this.history = history;
    }

    public String getQuery() {
        return query;
    }

    public List<HistoryItem> getHistory() {
        return history;
    }
}