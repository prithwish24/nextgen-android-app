package com.nextgen.carrental.app.android;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.model.Reservation;

import ai.api.ui.AIButton;

/**
 * View for Voice Chat
 * @author Pithwish
 */

public class FragmentVoiceChat extends Fragment implements View.OnClickListener {
    View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_voice_chat, container, false);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AIButton aiButton1 = getActivity().findViewById(R.id.micButton);

        AIButton aiButton = ((VoiceChatActivity)getActivity()).getAiButton();
        Reservation res = ((VoiceChatActivity)getActivity()).getReservation();

    }

    @Override
    public void onClick(View v) {
        final int itemId = v.getId();

    }
}
