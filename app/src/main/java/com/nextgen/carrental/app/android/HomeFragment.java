package com.nextgen.carrental.app.android;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.adapter.MessageListAdapter;
import com.nextgen.carrental.app.model.ChatMessage;
import com.nextgen.carrental.app.model.User;

import java.util.ArrayList;
import java.util.Date;


/**
 * Home activity - Voice chat
 * @author Prithwish
 */

public class HomeFragment extends Fragment {
    View homeView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.homeView = inflater.inflate(R.layout.fragment_home, container, false);
        return homeView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<ChatMessage> list = new ArrayList<ChatMessage>(){
            {
                User u1 = new User("admin", "Administrator", "MyAdmin", "admin@example.com");
                User u2 = new User("vagent", "Virtual Agent", "Agent John", "vagent@example.com");

                add(new ChatMessage("Hi", u1, new Date().getTime()));
                add(new ChatMessage("Hello", u2, new Date().getTime()+(10*1000)));
                add(new ChatMessage("Welcome to my world", u1, new Date().getTime()+(20*1000)));
                add(new ChatMessage("Thanks You", u2, new Date().getTime()+(25*1000)));
                add(new ChatMessage("Good Bye", u1, new Date().getTime()+(28*1000)));
            }
        };

        final RecyclerView recyclerView = homeView.findViewById(R.id.recycler_chat_window);
        MessageListAdapter messageListAdapter = new MessageListAdapter(getActivity(), list, "admin");
        recyclerView.setAdapter(messageListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
