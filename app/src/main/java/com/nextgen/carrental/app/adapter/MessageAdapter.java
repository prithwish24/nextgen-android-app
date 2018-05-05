package com.nextgen.carrental.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.model.ChatMessage;
import com.nextgen.carrental.app.util.Utils;

import java.util.List;

import static com.nextgen.carrental.app.model.ChatMessage.VIEW_TYPE_MESSAGE_RECEIVED;
import static com.nextgen.carrental.app.model.ChatMessage.VIEW_TYPE_MESSAGE_SENT;

/**
 * Adapter to display and recycle chat messages
 *
 * @author Prithwish
 */


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<ChatMessage> mMessages;

    public MessageAdapter(List<ChatMessage> messages) {
        this.mMessages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
        switch (viewType) {
            case VIEW_TYPE_MESSAGE_SENT:
                layout = R.layout.item_message_sent;
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                layout = R.layout.item_message_received;
                break;
        }
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ChatMessage chatMessage = mMessages.get(position);
        holder.bind(chatMessage);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }


    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getType();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mChatText;
        private TextView mTimeText;
        private TextView mAuthor;

        ViewHolder(View itemView) {
            super(itemView);
            mChatText = itemView.findViewById(R.id.message_text);
            mTimeText = itemView.findViewById(R.id.message_time);
            mAuthor = itemView.findViewById(R.id.message_author);
        }

        void bind(ChatMessage chatMessage) {
            mChatText.setText(chatMessage.getMessage());
            mTimeText.setText(Utils.fmtTime(chatMessage.getCreatedAt()));
            if (chatMessage.getType() == VIEW_TYPE_MESSAGE_RECEIVED) {
                mAuthor.setText(chatMessage.getSender());
            }
        }
    }
}
