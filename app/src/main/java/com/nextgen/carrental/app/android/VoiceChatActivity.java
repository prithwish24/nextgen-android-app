package com.nextgen.carrental.app.android;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.adapter.MessageListAdapter;
import com.nextgen.carrental.app.ai.Config;
import com.nextgen.carrental.app.android.tasks.GetUserSessionIdTask;
import com.nextgen.carrental.app.bo.BaseResponse;
import com.nextgen.carrental.app.bo.ZipCodeResponse;
import com.nextgen.carrental.app.model.Reservation;
import com.nextgen.carrental.app.service.RestClient;
import com.nextgen.carrental.app.util.GPSTracker;
import com.nextgen.carrental.app.util.PermissionManager;
import com.nextgen.carrental.app.util.TTS;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.PartialResultsListener;
import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIOutputContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.model.Status;
import ai.api.ui.AIButton;

public class VoiceChatActivity extends BaseActivity implements AIButton.AIButtonListener, PartialResultsListener {
    private static final String TAG = VoiceChatActivity.class.getName();
    //private Toolbar toolbar;

    public static String INITIAL_URL = "http://18.188.102.146:8002/zipcode/{sessionId}?zipcode={zipCode}";

    private PermissionManager permissionManager;
    private AIButton aiButton;
    private Handler handler;
    private String totalText;
    private MessageListAdapter messageListAdapter;
    private Reservation reservation;
    private TextView speechTextView;
    private FragmentVoiceChat fragmentVoiceChat;
    private FragmentConfirmation fragmentConfirmation;
    private long dateMe;

    //private Reservation reservation;

    public Reservation getReservation() {
        return reservation;
    }

    public AIButton getAiButton() {
        return aiButton;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.handler = new Handler(Looper.getMainLooper());
        this.permissionManager = PermissionManager.getInstance();
        this.messageListAdapter = new MessageListAdapter();
        this.aiButton = findViewById(R.id.micButton);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_voice_chat);

        /*toolbar = findViewById(R.id.app_bar_chat);
        setSupportActionBar(toolbar);*/

        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VoiceChatActivity.this, "Close clicked", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        findViewById(R.id.info_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VoiceChatActivity.this, "Info clicked", Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction()
                        .replace(R.id.vc_content_frame, new FragmentConfirmation())
                        .commit();
            }
        });

        this.fragmentVoiceChat = new FragmentVoiceChat();
        this.fragmentConfirmation = new FragmentConfirmation();
        getFragmentManager().beginTransaction()
                .replace(R.id.vc_content_frame, new FragmentVoiceChat())
                .commit();

        askPermissionToUser();
    }

    private void askPermissionToUser() {
        final String[] permissions = new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        permissionManager.requestPermission(
                permissions, this,
                new PermissionManager.CallbackHandler() {
                    @Override
                    public void onResponse(int requestCode, Activity activity) {
                        executeOnPermissionGranted();
                    }
                });
    }

    private void executeOnPermissionGranted() {
        Address currentLocation;
        if (permissionManager.hasPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                || permissionManager.hasPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            GPSTracker gpsTracker = new GPSTracker(this);
            final Location location = gpsTracker.getLocation();
            if (gpsTracker.isGPSServiceOn() && (location != null)) {
                currentLocation = gpsTracker.getAddress(location);
                if (currentLocation != null) {
                    Toast.makeText(getApplicationContext(), "Location Tracked: " + currentLocation.toString(),
                            Toast.LENGTH_LONG).show();
                }
            } else {
                gpsTracker.tryEnablingGPS(); // response will be displayed on onActivityResult method
            }
        } else {
            Toast.makeText(getApplicationContext(), "Location Access disabled.", Toast.LENGTH_SHORT).show();
        }

        if (permissionManager.hasPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            final String googleDialogFlowAccessToken = preferences.getString("dialogflow_agent_token", Config.ACCESS_TOKEN2);
            //Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
            initAIAgent(googleDialogFlowAccessToken);
        }

    }

    private void initAIAgent(final String googleDialogFlowAccessToken) {
        final String START_SPEECH = "Hi";
        AIConfiguration config = new AIConfiguration(
                googleDialogFlowAccessToken,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        config.setRecognizerStartSound(getResources().openRawResourceFd(R.raw.test_start));
        config.setRecognizerStopSound(getResources().openRawResourceFd(R.raw.test_stop));
        config.setRecognizerCancelSound(getResources().openRawResourceFd(R.raw.test_cancel));

        aiButton.initialize(config);
        aiButton.setResultsListener(this);
        aiButton.setPartialResultsListener(this);

        TTS.speak(START_SPEECH);

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        AIRequest firstRequest = new AIRequest();
        firstRequest.setQuery(START_SPEECH);
        new GetUserSessionIdTask().execute(firstRequest);
    }

    @Override
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "onResult");
                Log.i(TAG, "Received success response");
                final Status status = response.getStatus();
                Log.i(TAG, "Status code: " + status.getCode());
                Log.i(TAG, "Status type: " + status.getErrorType());

                final Result result = response.getResult();
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());

                Log.i(TAG, "Action: " + result.getAction());
                final String speech = result.getFulfillment().getSpeech();
                //resultTextView.setText(speech);
                Log.i(TAG, "Speech: " + speech);

                final ListIterator listIterator = result.getContexts().listIterator();
                while (listIterator.hasNext()) {
                    AIOutputContext context = (AIOutputContext) listIterator.next();
                    if (context.getName().equals("carrental")) {
                        if (context.getParameters().get("review") != null &&
                                context.getParameters().get("review").getAsBoolean()) {

                            //Intent intent = new Intent(HomeActivity.this, ShowReviewPageActivity.class);
                            Map<String, JsonElement> parameters = context.getParameters();
                            HashMap<String, String> data = new HashMap<>();
                            data.put("Location", parameters.get("pickuplocation").getAsJsonPrimitive().getAsString());
                            data.put("Date", parameters.get("pickupdate").getAsString());
                            data.put("Days", parameters.get("duration").getAsJsonObject().get("amount").getAsString());
                            data.put("Car", parameters.get("cartype").getAsJsonArray().get(0).getAsString());
//                            getFragmentManager().beginTransaction()
//                                    .replace(R.id.vc_content_frame, fragmentConfirmation)
//                                    .commit();
//                            assignConfirmationValues(data);
                            TTS.speak("Check the details and let me know if you wish to reserve");
                            //intent.putExtra("data", data);
                            //startActivity(intent);

                        } else {


                            /*ChatMessage chatMessageMe = new ChatMessage();
                            chatMessageMe.setMessage(totalText);
                            chatMessageMe.setDate(dateMe);
                            chatMessageMe.setMe(true);
                            if (!StringUtils.isEmpty(totalText)) {
                                displayMessage(chatMessageMe);
                            }

                            totalText="";
                            dateMe="";

                            ChatMessage chatMessageBot = new ChatMessage();
                            chatMessageBot.setId(122);//dummy
                            chatMessageBot.setMessage(speech);
                            chatMessageBot.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                            chatMessageBot.setMe(false);

                            displayMessage(chatMessageBot);
                            TTS.speak(speech);*/
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onError");
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                //resultTextView.setText(error.getMessage());
            }
        });
    }

    @Override
    public void onCancelled() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onCancelled");
                Toast.makeText(getApplicationContext(), "Action Cancelled", Toast.LENGTH_SHORT).show();
                //resultTextView.setText("Action Cancelled !");
            }
        });
    }

    @Override
    public void onPartialResults(List<String> partialResults) {
        final String result = partialResults.get(0);
        if (!TextUtils.isEmpty(result)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    totalText = result;
                    //dateMe = DateFormat.getDateTimeInstance().format(new Date());
                }
            });
        }
    }

    private class GetUserSessionIdTask extends AsyncTask<AIRequest, Void, AIResponse> {
        @Override
        protected AIResponse doInBackground(AIRequest... requests) {
            final AIRequest request = requests[0];
            try {
                aiButton.getAIService().resetContexts();
                final AIResponse response = aiButton.getAIService().textRequest(request);
                return response;
            } catch (AIServiceException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(AIResponse aiResponse) {
            if (aiResponse != null) {
                String sessionid = "";
                for (AIOutputContext a : aiResponse.getResult().getContexts()) {
                    if (a.getName().equals("carrental")) {
                        sessionid = a.getParameters().get("sessionId").getAsString();
                    }
                }

                try {
                    BaseResponse resp = RestClient.INSTANCE.getRequest(
                            INITIAL_URL, ZipCodeResponse.class, sessionid, "63001");
                    System.out.println(resp);
                } catch (Exception e) {

                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            executeOnPermissionGranted();
        } else {
//            Snackbar.make(findViewById(android.R.id.content), mErrorString.get(requestCode),
//                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent();
//                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            intent.addCategory(Intent.CATEGORY_DEFAULT);
//                            intent.setData(Uri.parse("package:" + getPackageName()));
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                            startActivity(intent);
//                        }
//                    }).show();
        }
    }

    private void assignConfirmationValues(HashMap<String, String> data) {
        fragmentConfirmation.getPickupLocation().setText(data.get("Location"));
        fragmentConfirmation.getPickupTime().setText(data.get("Date"));
        fragmentConfirmation.getReturnLocation().setText(data.get("Location"));
    }
}
