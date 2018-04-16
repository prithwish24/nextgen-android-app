package com.nextgen.carrental.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.model.ChatMessage;
import com.nextgen.carrental.app.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for chat conversation
 * @author Prithwish
 */

public class MessageListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private List<ChatMessage> mMessageList;

    public MessageListAdapter() {
        mMessageList = new ArrayList<>(0);
    }

    public void setMessageList(List<ChatMessage> mMessageList) {
        this.mMessageList = mMessageList;
        notifyDataSetChanged();

    }

    public void addMessage(ChatMessage mMessage) {
        this.mMessageList.add(mMessage);
        int position = mMessageList.size()-1;
        if (position < 0)   position = 0;
        notifyItemInserted (position);
    }

    @Override
    public int getItemViewType(int position) {
        final ChatMessage message = mMessageList.get(position);
        if (message.isSelfMessage()) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);

            return new SentMessageHolder(view);

        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);

            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ChatMessage chatMessage = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder)holder).bind(chatMessage);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder)holder).bind(chatMessage);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


    private static class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.sent_message_text);
            timeText = itemView.findViewById(R.id.sent_message_time);
        }

        void bind (ChatMessage cMsg) {
            messageText.setText(cMsg.getMessage());
            timeText.setText(Utils.fmtTime(cMsg.getCreatedAt(), Utils.SHORT_TIME_AMPM));
        }
    }

    private static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView messageText;
        TextView timeText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.received_message_name);
            messageText =  itemView.findViewById(R.id.received_message_text);
            timeText = itemView.findViewById(R.id.received_message_time);
        }

        void bind (ChatMessage cMsg) {
            if (TextUtils.isEmpty(cMsg.getSender().getNickname())) {
                nameText.setText(cMsg.getSender().getName());
            } else {
                nameText.setText(cMsg.getSender().getNickname());
            }

            messageText.setText(cMsg.getMessage());
            timeText.setText(Utils.fmtTime(cMsg.getCreatedAt(), Utils.SHORT_TIME_AMPM));
        }
    }
}
