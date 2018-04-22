package com.nextgen.carrental.app.android;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.nextgen.carrental.app.R;

public class VoiceChatActivity extends BaseActivity {
    //private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        getFragmentManager().beginTransaction()
                .replace(R.id.vc_content_frame, new FragmentVoiceChat())
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
