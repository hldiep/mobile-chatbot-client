package com.example.mychatbot.models;
import com.google.gson.annotations.SerializedName;
public class ChatResponse {
    @SerializedName("data")
    private String reply;

    public String getReply() {
        return reply;
    }
}
