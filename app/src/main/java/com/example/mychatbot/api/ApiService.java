package com.example.mychatbot.api;


import com.example.mychatbot.models.ChatResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/v1/ai/chat-2")
    Call<ChatResponse> sendMessage(@Query("query") String queryText);
}
