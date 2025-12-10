package com.example.mychatbot.api;


import com.example.mychatbot.models.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/v1/ai/chat-2")
    Call<ChatResponse> sendMessage(@Body ChatRequest request);
}
