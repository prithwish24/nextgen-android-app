package com.nextgen.carrental.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.model.ChatMessage;
import com.nextgen.carrental.app.util.Utils;

import java.util.List;

/**
 * Adapter for chat conversation
 * @author Prithwish
 */

public class MessageListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    //private Context mContext;
    private List<ChatMessage> mMessageList;
    private String mUserId;

    public MessageListAdapter(Context context, List<ChatMessage> messageList, String selfUserId) {
        //mContext = context;
        mMessageList = messageList;
        mUserId = selfUserId;
    }

    @Override
    public int getItemViewType(int position) {
        final ChatMessage message = mMessageList.get(position);

        if (message.getSender().getUserId().equals(mUserId) ) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

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

        public SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.sent_message_text);
            timeText = (TextView) itemView.findViewById(R.id.sent_message_time);
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

        public ReceivedMessageHolder(View itemView) {
            super(itemView);

            nameText = (TextView) itemView.findViewById(R.id.received_message_name);
            messageText = (TextView) itemView.findViewById(R.id.received_message_text);
            timeText = (TextView) itemView.findViewById(R.id.received_message_time);
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
