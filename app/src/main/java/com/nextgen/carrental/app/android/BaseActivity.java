/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nextgen.carrental.app.android;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.ai.AIApplication;
import com.nextgen.carrental.app.util.PermissionManager;
import com.nextgen.carrental.app.util.SessionManager;
import com.nextgen.carrental.app.util.TTS;

public abstract class BaseActivity extends AppCompatActivity {

    private AIApplication app;

    private static final long PAUSE_CALLBACK_DELAY = 500;
    private static final int REQUEST_AUDIO_PERMISSIONS_ID = 33;

    private final Handler handler = new Handler();
    private Runnable pauseCallback = new Runnable() {
        @Override
        public void run() {
            app.onActivityPaused();
        }
    };

    protected SessionManager sessionManager;
    protected PermissionManager permissionManager;
    private SparseIntArray mErrorString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mErrorString = new SparseIntArray();

        sessionManager = new SessionManager(getApplicationContext());
        permissionManager = new PermissionManager();

        app = (AIApplication) getApplication();
        TTS.init(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //checkAudioRecordPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        app.onActivityResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.postDelayed(pauseCallback, PAUSE_CALLBACK_DELAY);
    }

    public SessionManager getAppSessionManager() {
        return sessionManager;
    }

    protected void showUserDetailsOnDrawer(final NavigationView.OnNavigationItemSelectedListener navView) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(navView);

        final View headerView = navigationView.getHeaderView(0);
        final TextView textViewName = headerView.findViewById(R.id.textView_name);
        final TextView textViewEmail = headerView.findViewById(R.id.textView_email);
        textViewName.setText(sessionManager.getData(SessionManager.KEY_NAME));
        textViewEmail.setText(sessionManager.getData(SessionManager.KEY_EMAIL));

        final ImageView imageViewProfile = headerView.findViewById(R.id.imageView_profile);
        imageViewProfile.setImageResource(R.mipmap.ic_profile);

    }

}
