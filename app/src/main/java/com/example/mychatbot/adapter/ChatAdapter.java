package com.example.mychatbot.adapter;

import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatbot.R;
import com.example.mychatbot.models.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_USER = 1;
    private static final int TYPE_BOT = 2;

    private List<ChatMessage> chatList;

    public ChatAdapter(List<ChatMessage> chatList) {
        this.chatList = chatList;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = chatList.get(position);
        if (message.isUser()) {
            return TYPE_USER;
        } else {
            return TYPE_BOT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_item, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bot_item, parent, false);
            return new BotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = chatList.get(position);

        if (holder.getItemViewType() == TYPE_USER) {
            ((UserViewHolder) holder).bind(message);
        } else {
            ((BotViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        if (chatList == null) return 0;
        return chatList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView textMessage;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
        }

        void bind(ChatMessage message) {
            textMessage.setText(message.getMessage());
        }
    }

    static class BotViewHolder extends RecyclerView.ViewHolder {
        private TextView textMessage;

        BotViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
        }

        void bind(ChatMessage message) {
            String text = message.getMessage().replace("LLM response:", "").trim();
            textMessage.setText(parseMarkdown(text));
        }

        // Chuyển Markdown đơn giản sang Spanned (bold + bullet + xuống dòng)
        private CharSequence parseMarkdown(String input) {
            android.text.SpannableStringBuilder builder = new android.text.SpannableStringBuilder();
            String[] lines = input.split("\n");

            for (String line : lines) {
                int start = builder.length();

                if (line.contains("**")) {
                    line = line.replace("**", "");
                    builder.append(line);
                    builder.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                            start, start + line.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (line.trim().startsWith("*")) {
                    builder.append("• " + line.replace("*", "").trim());
                } else {
                    builder.append(line);
                }
                builder.append("\n");
            }
            return builder;
        }
    }

}