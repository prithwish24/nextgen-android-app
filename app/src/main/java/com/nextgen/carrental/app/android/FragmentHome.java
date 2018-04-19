package com.nextgen.carrental.app.android;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nextgen.carrental.app.R;


/**
 * Home - User Home after login
 * @author Prithwish
 */

public class FragmentHome extends Fragment {
    private static final String TAG = FragmentHome.class.getName();
    private View homeView;
    /*private MainActivity parent;
    private AIButton aiButton;
    private MessageListAdapter mMessageListAdapter;
    private TextView textViewChat;
    private PermissionManager permissionManager;
    private Handler handler;*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.homeView = inflater.inflate(R.layout.fragment_home, container, false);
        /*this.aiButton = homeView.findViewById(R.id.micButton);
        this.textViewChat = homeView.findViewById(R.id.textView_chat);
        this.parent = (MainActivity) getActivity();
        this.handler = new Handler(Looper.getMainLooper());
        this.permissionManager = PermissionManager.getInstance();*/
        return homeView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) homeView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                /*getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FragmentVoiceChat())
                        .commit();*/
                startActivity(new Intent(getActivity().getApplicationContext(), VoiceChatActivity.class));
            }
        });

        /*mMessageListAdapter = new MessageListAdapter();
        final RecyclerView recyclerView = homeView.findViewById(R.id.recycler_chat_window);
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
                });*/

    }

    /*private void onPermissionGranted() {
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

            aiButton.setPartialResultsListener(new MyResultListener());
        }

    }*/

    /*@Override
    public void onStart() {
        super.onStart();
        //aiButton.startListening();
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
        parent.runOnUiThread(new Runnable() {
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
    }*/

    /*@Override
    public void onError(final AIError error) {
        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onError");
                *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*//*
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                //resultTextView.setText(error.getMessage());
            }
        });
    }

    @Override
    public void onCancelled() {
        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onCancelled");
                Toast.makeText(getActivity().getApplicationContext(), "Action Cancelled", Toast.LENGTH_SHORT).show();
                //resultTextView.setText("Action Cancelled !");
            }
        });
    }

    @Override
    public void onAudioLevel(float level) {

    }



    @Override
    public void onListeningStarted() {
        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void onListeningCanceled() {
        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void onListeningFinished() {
        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ChatMessage chat = new ChatMessage(textViewChat.getText().toString(), true);
                mMessageListAdapter.addMessage(chat);
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
    }*/

}
