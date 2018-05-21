package com.nextgen.carrental.app.android;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.ai.Config;
import com.nextgen.carrental.app.bo.BaseResponse;
import com.nextgen.carrental.app.bo.ZipCodeResponse;
import com.nextgen.carrental.app.constants.GlobalConstants;
import com.nextgen.carrental.app.model.BookingData;
import com.nextgen.carrental.app.model.ChatMessage;
import com.nextgen.carrental.app.service.RestClient;
import com.nextgen.carrental.app.service.RestParameter;
import com.nextgen.carrental.app.util.AIResponseTransformer;
import com.nextgen.carrental.app.util.GPSTracker;
import com.nextgen.carrental.app.util.PermissionManager;
import com.nextgen.carrental.app.util.SessionManager;
import com.nextgen.carrental.app.util.TTS;
import com.nextgen.carrental.app.util.Utils;

import org.springframework.core.ParameterizedTypeReference;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import ai.api.AIServiceException;
import ai.api.PartialResultsListener;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIOutputContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.ui.AIButton;

public class VoiceChatActivity extends BaseActivity
        implements AIButton.AIButtonListener,
        PartialResultsListener, View.OnClickListener {
    private static final String TAG = VoiceChatActivity.class.getName();

    //public static String INITIAL_URL = "http://18.188.102.146:8002/zipcode/{sessionId}?zipcode={zipCode}";
    final String START_SPEECH = "Hi";

    private PermissionManager permissionManager;
    private AIButton aiButton;
    private Handler handler;
    private FragmentVoiceChat fragmentVoiceChat;
    private FragmentConfirmation fragmentConfirmation;
    public Address currentGpsAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.handler = new Handler(Looper.getMainLooper());
        this.permissionManager = PermissionManager.getInstance();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_voice_chat);

        //SharedPreferences pref = getApplicationContext().getSharedPreferences(SessionManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //sessionId = pref.getString(SessionManager.SESSION_KEY_ID,"");
        //Utils.getSettingsValue(getApplicationContext(), SessionManager.SESSION_KEY_ID);

        this.aiButton = findViewById(R.id.aiMicButton);
        findViewById(R.id.close_button).setOnClickListener(this);
        findViewById(R.id.info_button).setOnClickListener(this);

        this.fragmentVoiceChat = new FragmentVoiceChat();
        this.fragmentConfirmation = new FragmentConfirmation();

        // Clear existing history
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        getFragmentManager().beginTransaction()
                .replace(R.id.vc_content_frame, fragmentVoiceChat, FragmentVoiceChat.TAG)
                .addToBackStack(null)
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
        Address curLoc;
        if (permissionManager.hasPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                || permissionManager.hasPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            GPSTracker gpsTracker = new GPSTracker(this);
            final Location location = gpsTracker.getLocation();
            if (gpsTracker.isGPSServiceOn() && (location != null)) {
                curLoc = gpsTracker.getAddress(location);
                if (curLoc != null) {
                    this.currentGpsAddress = curLoc;
                    StringBuilder tmp = new StringBuilder("Location Tracked: ")
                            .append(curLoc.getLocality()).append(",")
                            .append(curLoc.getPostalCode()).append(",")
                            .append(curLoc.getCountryCode());
                    Toast.makeText(getApplicationContext(), tmp, Toast.LENGTH_LONG).show();
                }
            } else {
                gpsTracker.tryEnablingGPS(); // response will be displayed on onActivityResult method
            }
        } else {
            Toast.makeText(getApplicationContext(), "Location Access disabled.", Toast.LENGTH_SHORT).show();
        }

        if (permissionManager.hasPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)) {
            String dialogFlowAccessToken = Utils.getSettingsValue(getApplicationContext(),
                    GlobalConstants.AppSettings.DialogFlowClientToken);

            if (dialogFlowAccessToken == null || dialogFlowAccessToken.isEmpty())
                dialogFlowAccessToken = Config.ACCESS_TOKEN2;

            initAIAgent(dialogFlowAccessToken);
        }

    }

    private void initAIAgent(final String googleDialogFlowAccessToken) {
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

        //TTS.speak(START_SPEECH);

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        AIRequest firstRequest = new AIRequest();
        firstRequest.setQuery(START_SPEECH);
        new GetUserSessionIdTask(getApplicationContext(), currentGpsAddress).execute();
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
                addSpeechToChatRoster();

                //Log.i(TAG, "onResult");
                Log.i(TAG, "Received success response");
                //final Status status = response.getStatus();
                //Log.i(TAG, "Status code: " + status.getCode());
                //Log.i(TAG, "Status type: " + status.getErrorType());

                final Result result = response.getResult();
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());

                Log.i(TAG, "Action: " + result.getAction());
                final String speech = result.getFulfillment().getSpeech();
                final String displayText = result.getFulfillment().getDisplayText();
                Log.i(TAG, "Speech: " + speech);
                Log.i(TAG, "Speech: " + displayText);

                addBoTResponseToChatRoster(TextUtils.isEmpty(displayText) ? speech : displayText);
                TTS.speak(speech);

                final ListIterator listIterator = result.getContexts().listIterator();
                while (listIterator.hasNext()) {
                    AIOutputContext context = (AIOutputContext) listIterator.next();

                    if (context.getName().equals("carrental")) {
                        final Map<String, JsonElement> parameters = context.getParameters();
                        final String step = (parameters.get("step") != null)
                                ? parameters.get("step").getAsString().toLowerCase() : null;

                        if (TextUtils.equals(step, "review")) {
                            /*
                                1. "Please check your booking information...." should come from bot.
                                2. Reservation should be replace with BookingData object
                                3. Date-time need to be in Date/Long format so that can be formatted easily.
                                4. Need some way to display car class desc to be populated as per car type
                                5. Same for car image. We can restrict our use case till 3 car classes
                                   as we have only 3 pictures in res folder.
                                6.
                             */

                            final BookingData bookingData = new AIResponseTransformer().transform(parameters);
                            fragmentConfirmation.bindConfirmationData(bookingData);

                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.vc_content_frame, fragmentConfirmation, FragmentConfirmation.TAG)
                                    .addToBackStack(null)
                                    .commit();

                            //addBoTResponseToChatRoster(displayText);
                            //TTS.speak(speech);
                            //TTS.speak("Please review your booking information. Would you like to confirm this booking?");

                        } else if (TextUtils.equals(step, "confirmation")) {
                            final BookingData bookingData = new AIResponseTransformer().transform(parameters);
                            fragmentConfirmation.updateConfirmationNumber("99024651");

                            //addBoTResponseToChatRoster(displayText);
                            //TTS.speak(speech);

                            /*Fragment fragment = getFragmentManager().findFragmentByTag(FragmentConfirmation.TAG);
                            final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.detach(fragment).commit();

                            final BookingData bookingData = new AIResponseTransformer().transform(parameters);
                            fragmentConfirmation.bindConfirmationData(bookingData);

                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.vc_content_frame, fragmentConfirmation, FragmentConfirmation.TAG)
                                    .commit();*/
                            /*final Fragment fragment = getFragmentManager().findFragmentByTag(FragmentConfirmation.TAG);
                            final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.detach(fragment);
                            transaction.attach(fragment);
                            transaction.commit();*/

                        /*} else {
                            addBoTResponseToChatRoster(displayText);
                            TTS.speak(speech);*/

                        }
                    }
                }

                /*
                 *  ---- FINAL STEP ----
                 *  1. Context is missing here. Need context till RES# generates.
                 *  2. Need some flag like 'review' to know the end step.
                 *  3. CONF# should be somewhere to extract easily and pass into the fragment
                 *     which replace green highlighted section
                 *  4. speech should not contain CONF#. It feels ugly to hear.
                 */

            }
        });
    }

    @Override
    public void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onError");
                /*Snackbar.make(findViewById(android.R.id.content), "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                addSpeechToChatRoster();
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
                addSpeechToChatRoster();
            }
        });
    }

    private void addBoTResponseToChatRoster(String responseText) {
        if (this.fragmentVoiceChat != null) {
            if (responseText != null) {
                fragmentVoiceChat.addMessage(new ChatMessage(responseText, GlobalConstants.AGENT_NAME));
            }
        }
    }

    private void addSpeechToChatRoster() {
        if (this.fragmentVoiceChat != null) {
            final TextView speechTextBox = findViewById(R.id.text_view_speech);
            CharSequence speech = speechTextBox.getText();
            speechTextBox.setText("");
            if (!TextUtils.isEmpty(speech)) {
                fragmentVoiceChat.addMessage(new ChatMessage(speech.toString(), true));
            }
        }
    }


    @Override
    public void onPartialResults(List<String> partialResults) {
        final String result = partialResults.get(0);
        if (!TextUtils.isEmpty(result)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    final TextView speechTextView = findViewById(R.id.text_view_speech);
                    speechTextView.setText(result);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        switch (viewId) {
            case R.id.close_button:
                Toast.makeText(VoiceChatActivity.this, "Close clicked", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
            case R.id.info_button:
                Toast.makeText(VoiceChatActivity.this, "Info clicked", Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction()
                        .replace(R.id.vc_content_frame, new FragmentConfirmation())
                        .addToBackStack(null)
                        .commit();
                break;
            default:
                Toast.makeText(VoiceChatActivity.this, "Unassigned Event", Toast.LENGTH_SHORT).show();
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
            Snackbar.make(
                    findViewById(android.R.id.content),
                    "Please enable required permissions to use this app.",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("ENABLE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    private class GetUserSessionIdTask extends AsyncTask<Void, Void, AIResponse> {
        private WeakReference<Context> contextRef;
        private WeakReference<Address> addressRef;

        GetUserSessionIdTask(Context context, Address address) {
            this.contextRef = new WeakReference<>(context);
            this.addressRef = new WeakReference<>(address);
        }

        @Override
        protected AIResponse doInBackground(Void... requests) {
            try {
                final String sessionID = new SessionManager(contextRef.get()).getLoggedInSessionID();
                final Map<String, String> maps = new HashMap<>(1);
                maps.put(SessionManager.SESSION_KEY_ID, sessionID);

                final AIContext aiContext = new AIContext("CarRental");
                aiContext.setParameters(maps);
                final RequestExtras requestExtras = new RequestExtras(Collections.singletonList(aiContext), null);

                aiButton.getAIService().resetContexts();
                final AIResponse response = aiButton.getAIService().textRequest(START_SPEECH, requestExtras);
                return response;

            } catch (AIServiceException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(AIResponse aiResponse) {
            if (aiResponse != null) {
                try {
                    final String svcURL = Utils.getServiceURL(contextRef.get(),
                            GlobalConstants.Services.SAVE_DUMMY_ZIPCODE);
                    final String sessionID = new SessionManager(contextRef.get()).getLoggedInSessionID();

                    String zipCode;
                    if (Utils.isPreferenceSwitchedOn(contextRef.get(), GlobalConstants.AppSettings.switchZipCodeMocking)) {
                        zipCode = Utils.getSettingsValue(contextRef.get(), GlobalConstants.AppSettings.MockZipCode);
                    } else {
                        Address address = addressRef.get();
                        zipCode = address.getPostalCode();
                    }

                    final RestParameter params = new RestParameter();
                    params.addQueryParam("zipcode", zipCode);
                    params.addPathParam("sessionId", sessionID);

                    ParameterizedTypeReference<BaseResponse<ZipCodeResponse>> typeRef
                            = new ParameterizedTypeReference<BaseResponse<ZipCodeResponse>>() {};

                    BaseResponse<ZipCodeResponse> resp = RestClient.INSTANCE.GET(svcURL, params, typeRef);
                    Log.i(TAG, resp.toString());

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }

}
