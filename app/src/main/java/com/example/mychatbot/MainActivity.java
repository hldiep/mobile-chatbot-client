package com.example.mychatbot;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatbot.adapter.ChatAdapter;
import com.example.mychatbot.api.ApiClient;
import com.example.mychatbot.api.ApiService;
import com.example.mychatbot.models.ChatMessage;
import com.example.mychatbot.models.ChatResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private ImageButton buttonSend;

    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatAdapter);

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                // Nếu chiều cao bottom nhỏ hơn oldBottom -> Bàn phím đang hiện lên
                if (bottom < oldBottom) {
                    if (messageList.size() > 0) {
                        if (!recyclerView.canScrollVertically(-1)) {
                            recyclerView.postDelayed(() ->
                                            recyclerView.smoothScrollToPosition(messageList.size() - 1)
                                    , 100);
                        }
                    }
                }
            }
        });

        apiService = ApiClient.getClient().create(ApiService.class);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

//    private void sendMessage() {
//        String msgContent = editTextMessage.getText().toString().trim();
//
//        if (TextUtils.isEmpty(msgContent)) {
//            return;
//        }
//
//        addMessage(msgContent, true);
//        editTextMessage.setText("");
//
//        ChatMessage loadingMessage = new ChatMessage("Đang xử lý ...", false);
//        messageList.add(loadingMessage);
//        chatAdapter.notifyItemInserted(messageList.size() - 1);
//        recyclerView.scrollToPosition(messageList.size() - 1);
//
//        // Gọi API
//        callChatApi(msgContent, loadingMessage);
//    }
    private void sendMessage() {
        String msgContent = editTextMessage.getText().toString().trim();

        if (TextUtils.isEmpty(msgContent)) {
            return;
        }

        addMessage(msgContent, true);
        editTextMessage.setText("");

        // Tạo loading message
        final ChatMessage loadingMessage = new ChatMessage(".", false);
        messageList.add(loadingMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);

        // -----------------------------
        // Handler để làm nháy dấu 3 chấm
        // -----------------------------
        final Handler handler = new Handler();
        final String[] dots = {".", ". .", ". . ."};
        final int[] index = {0};

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadingMessage.setMessage(dots[index[0]]);
                chatAdapter.notifyItemChanged(messageList.indexOf(loadingMessage));
                index[0] = (index[0] + 1) % dots.length;
                handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);

        // Gọi API
        callChatApi(msgContent, loadingMessage);

        // Khi API trả về xong, remove loading và stop animation
        loadingMessage.setHandler(handler);
        loadingMessage.setRunnable(runnable);
    }

    // Hàm gọi API
    private void callChatApi(String question, ChatMessage loadingMsg) {
        apiService.sendMessage(question).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {

                removeLoadingMessage(loadingMsg);

                if (response.isSuccessful() && response.body() != null) {
                    String reply = response.body().getReply();
                    if (reply != null) {
                        addMessage(reply, false);
                    } else {
                        addMessage("Bot trả về dữ liệu rỗng.", false);
                    }
                } else {
                    addMessage("Lỗi Server: " + response.code(), false);
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                removeLoadingMessage(loadingMsg);
                addMessage("Lỗi kết nối: " + t.getMessage(), false);
            }
        });
    }

    private void addMessage(String message, boolean isUser) {
        messageList.add(new ChatMessage(message, isUser));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }

//    private void removeLoadingMessage(ChatMessage loadingMsg) {
//        int position = messageList.indexOf(loadingMsg);
//        if (position != -1) {
//            messageList.remove(position);
//            chatAdapter.notifyItemRemoved(position);
//        }
//    }
    private void removeLoadingMessage(ChatMessage loadingMsg) {
        // Stop nháy chấm
        loadingMsg.stopAnimation();

        int position = messageList.indexOf(loadingMsg);
        if (position != -1) {
            messageList.remove(position);
            chatAdapter.notifyItemRemoved(position);
        }
    }
}