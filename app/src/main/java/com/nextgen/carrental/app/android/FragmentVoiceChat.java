package com.nextgen.carrental.app.android;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.adapter.MessageListAdapter;
import com.nextgen.carrental.app.ai.Config;
import com.nextgen.carrental.app.model.ChatMessage;
import com.nextgen.carrental.app.model.User;
import com.nextgen.carrental.app.util.GPSTracker;
import com.nextgen.carrental.app.util.PermissionManager;
import com.nextgen.carrental.app.util.TTS;

import java.util.List;

import ai.api.AIListener;
import ai.api.PartialResultsListener;
import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.model.Status;
import ai.api.ui.AIButton;

/**
 * View of Chat Window
 * @author Prithwish
 *
 */

public class FragmentVoiceChat extends Fragment implements AIButton.AIButtonListener {
    private static final String TAG = FragmentVoiceChat.class.getName();
    private View view;

    private AIButton aiButton;
    private MessageListAdapter mMessageListAdapter;
    private TextView textViewChat;
    private PermissionManager permissionManager;
    private Handler handler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_voice_chat, container, false);
        this.aiButton = view.findViewById(R.id.micButton);
        this.textViewChat = view.findViewById(R.id.textView_chat);
        this.handler = new Handler(Looper.getMainLooper());
        this.permissionManager = PermissionManager.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mMessageListAdapter = new MessageListAdapter();
        final RecyclerView recyclerView = this.view.findViewById(R.id.recycler_chat_window);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mMessageListAdapter);

        final String [] permissions = new String[] {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        permissionManager.requestPermission(
                permissions, getActivity(),
                new PermissionManager.CallbackHandler() {
                    @Override
                    public void onResponse(int requestCode, Activity activity) {
                        onPermissionGranted();
                    }
                });

        /*ArrayList<ChatMessage> list = new ArrayList<ChatMessage>(){
            {
                User u1 = new User("admin", "Administrator", "MyAdmin", "admin@example.com");
                User u2 = new User("vagent", "Virtual Agent", "Agent John", "vagent@example.com");

                add(new ChatMessage("Hi", u1, new Date().getTime()));
                add(new ChatMessage("Hello", u1, new Date().getTime()));
                add(new ChatMessage("Welcome to my world", u2, new Date().getTime()));
                add(new ChatMessage("Thanks You", u2, new Date().getTime()));
                add(new ChatMessage("Good Bye", u1, new Date().getTime()));
            }
        };

        mMessageListAdapter.setMessageList(list);*/


    }

    private void onPermissionGranted() {
        Address currentLocation;
        if (permissionManager.hasPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                || permissionManager.hasPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            GPSTracker gpsTracker = new GPSTracker(getActivity());
            final Location location = gpsTracker.getLocation();
            if (gpsTracker.isGPSServiceOn() && (location != null) ) {
                currentLocation = gpsTracker.getAddress(location);
                Toast.makeText(getActivity(), "Location Tracked: "+currentLocation.toString(),
                        Toast.LENGTH_LONG).show();
            } else {
                gpsTracker.tryEnablingGPS(); // response will be displayed on onActivityResult method
            }
        } else {
            Toast.makeText(getActivity(), "Location Access disabled.", Toast.LENGTH_SHORT).show();
        }


        if (permissionManager.hasPermission(getContext(), Manifest.permission.RECORD_AUDIO)) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
            final String accessToken = preferences.getString("dialogflow_agent_token", "");
            //Toast.makeText(this, key, Toast.LENGTH_SHORT).show();

            AIConfiguration config;
            if (!accessToken.trim().isEmpty()) {
                config = new AIConfiguration(
                        Config.ACCESS_TOKEN,
                        AIConfiguration.SupportedLanguages.English,
                        AIConfiguration.RecognitionEngine.System);
            } else {
                config = new AIConfiguration(
                        Config.ACCESS_TOKEN,
                        AIConfiguration.SupportedLanguages.English,
                        AIConfiguration.RecognitionEngine.System);
            }
            config.setRecognizerStartSound(getResources().openRawResourceFd(R.raw.test_start));
            config.setRecognizerStopSound(getResources().openRawResourceFd(R.raw.test_stop));
            config.setRecognizerCancelSound(getResources().openRawResourceFd(R.raw.test_cancel));

            aiButton.initialize(config);
            aiButton.setResultsListener(this);

            aiButton.setPartialResultsListener(new FragmentVoiceChat.MyResultListener());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        aiButton.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (aiButton != null) {
            aiButton.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (aiButton != null) {
            aiButton.resume();
        }
    }

    @Override
    public void onResult(final AIResponse response) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Log.i(TAG, "onResult");
                Log.i(TAG, "Received success response");

                //resultTextView.setText(gson.toJson(response));

                // this is example how to get different parts of result object
                final Status status = response.getStatus();
                Log.i(TAG, "Status code: " + status.getCode());
                Log.i(TAG, "Status type: " + status.getErrorType());

                final Result result = response.getResult();
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());

                Log.i(TAG, "Action: " + result.getAction());
                final String speech = result.getFulfillment().getSpeech();
                //resultTextView.setText(speech);
                Log.i(TAG, "Speech: " + speech);

                User vAgent = new User("vagent", "Virtual Agent", "Agent John", "vagent@example.com");
                ChatMessage chat = new ChatMessage(speech, vAgent);
                mMessageListAdapter.addMessage(chat);
                TTS.speak(speech);
            }
        });
    }

    @Override
    public void onError(final AIError error) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onError");
                Snackbar.make(view, error.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("Error", null).show();
                //Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCancelled() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onCancelled");
                Toast.makeText(getActivity().getApplicationContext(), "Action Cancelled", Toast.LENGTH_SHORT).show();
                //resultTextView.setText("Action Cancelled !");
            }
        });
    }


    class MyResultListener implements PartialResultsListener {
        @Override
        public void onPartialResults(List<String> partialResults) {
            final String result = partialResults.get(0);
            if (!TextUtils.isEmpty(result)) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textViewChat.setText(result);
                    }
                });
            }
        }
    }

    class MyAIListener implements AIListener {

        @Override
        public void onResult(AIResponse result) {

        }

        @Override
        public void onError(AIError error) {

        }

        @Override
        public void onAudioLevel(float level) {

        }

        @Override
        public void onListeningStarted() {

        }

        @Override
        public void onListeningCanceled() {

        }

        @Override
        public void onListeningFinished() {

        }
    }

}
