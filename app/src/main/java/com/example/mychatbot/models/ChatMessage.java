package com.example.mychatbot.models;

import android.os.Handler;

public class ChatMessage {
    private String message;
    private boolean isUser;
    private Handler handler;
    private Runnable runnable;

    public void setHandler(Handler handler) { this.handler = handler; }
    public void setRunnable(Runnable runnable) { this.runnable = runnable; }
    public void stopAnimation() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUser() {
        return isUser;
    }
}