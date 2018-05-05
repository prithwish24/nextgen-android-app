package com.nextgen.carrental.app.android;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.adapter.MessageAdapter;
import com.nextgen.carrental.app.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;


/**
 * View for Voice Chat
 * @author Pithwish
 */

public class FragmentVoiceChat extends Fragment {
    public static final String TAG = FragmentVoiceChat.class.getName();

    private RecyclerView mRecyclerView;
    private MessageAdapter mMessageAdapter;
    private List<ChatMessage> mChatMessages = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMessageAdapter = new MessageAdapter(mChatMessages);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voice_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recycler_chat_window);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mMessageAdapter);
    }

    public void addMessage(ChatMessage chatMessage) {
        mChatMessages.add(chatMessage);
        mMessageAdapter.notifyItemChanged(mChatMessages.size() - 1);
        scrollToBottom();
    }

    private void scrollToBottom() {
        mRecyclerView.scrollToPosition(mMessageAdapter.getItemCount() - 1);
    }
}
