package com.nextgen.carrental.app.android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.adapter.MessageListAdapter;
import com.nextgen.carrental.app.android.tasks.GetUserSessionIdTask;
import com.nextgen.carrental.app.bo.BaseResponse;
import com.nextgen.carrental.app.bo.ZipCodeResponse;
import com.nextgen.carrental.app.service.RestClient;
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

public class VoiceChatActivity extends BaseActivity implements AIButton.AIButtonListener {
    private static final String TAG = VoiceChatActivity.class.getName();
    //private Toolbar toolbar;

    public static String INITIAL_URL = "http://18.188.102.146:8002/zipcode/{sessionId}?zipcode={zipCode}";

    private AIButton aiButton;
    private Handler handler;
    private String totalText;
    private String dateMe;
    private MessageListAdapter messageListAdapter;

    public AIButton getAiButton() {
        return aiButton;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.handler = new Handler(Looper.getMainLooper());
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_voice_chat);

        messageListAdapter = new MessageListAdapter();
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

        getFragmentManager().beginTransaction()
                .replace(R.id.vc_content_frame, new FragmentVoiceChat())
                .commit();

        initAIAgent();

    }

    private void initAIAgent() {
        final String START_SPEECH = "Hi";
        aiButton = findViewById(R.id.micButton);

        AIConfiguration config = new AIConfiguration(
                "c33cc5dc601d48799b48f085e1360e59",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        config.setRecognizerStartSound(getResources().openRawResourceFd(R.raw.test_start));
        config.setRecognizerStopSound(getResources().openRawResourceFd(R.raw.test_stop));
        config.setRecognizerCancelSound(getResources().openRawResourceFd(R.raw.test_cancel));

        aiButton.initialize(config);
        aiButton.setResultsListener(this);
        aiButton.setPartialResultsListener(new PartialResultsListener() {
            @Override
            public void onPartialResults(List<String> partialResults) {
                final String result = partialResults.get(0);
                if (!TextUtils.isEmpty(result)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            totalText=result;
                            dateMe = DateFormat.getDateTimeInstance().format(new Date());
                        }
                    });
                }
            }
        });

        TTS.speak(START_SPEECH);

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        AIRequest firstRequest = new AIRequest();
        firstRequest.setQuery(START_SPEECH);
        new GetUserSessionIdTask().execute(firstRequest);

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
                while(listIterator.hasNext()) {
                    AIOutputContext context = (AIOutputContext) listIterator.next();
                    if(context.getName().equals("carrental")){
                        if(context.getParameters().get("review") != null &&
                                context.getParameters().get("review").getAsBoolean()){

                            //Intent intent = new Intent(HomeActivity.this, ShowReviewPageActivity.class);
                            Map<String,JsonElement> parameters  = context.getParameters();
                            HashMap<String,String> data = new HashMap<>();
                            data.put("Location",parameters.get("pickuplocation").getAsJsonPrimitive().getAsString());
                            data.put("Date",parameters.get("pickupdate").getAsString());
                            data.put("Days",parameters.get("duration").getAsJsonObject().get("amount").getAsString());
                            data.put("Car",parameters.get("cartype").getAsJsonArray().get(0).getAsString());
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

    private class GetUserSessionIdTask extends AsyncTask<AIRequest,Void,AIResponse> {
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
}
