package com.nextgen.carrental.app.android.tasks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.android.MainActivity;
import com.nextgen.carrental.app.bo.BaseResponse;
import com.nextgen.carrental.app.bo.UserProfile;
import com.nextgen.carrental.app.service.RestClient;
import com.nextgen.carrental.app.util.SessionManager;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * User Login Task
 * @author Prithwish
 */

public class UserLoginTask extends AsyncTask<Void, Void, BaseResponse<UserProfile>> {
    private static final String[] DUMMY_CREDENTIALS = new String[]{ "admin:admin" };
    private static final String TAG = UserLoginTask.class.getName();

    private final String mUsername;
    private final String mPassword;
    private final Context context;
    private final Activity activity;


    public UserLoginTask(String username, String password, Activity activity) {
        this.mUsername = username;
        this.mPassword = password;
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    @Override
    protected BaseResponse<UserProfile> doInBackground(Void... params) {
        final String loginServiceURL = "https://nextgen-gateway.herokuapp.com/ngapi/";
        BaseResponse<UserProfile> response = null;

        // Attempt 1 - using dev credentials
        for (String credential : DUMMY_CREDENTIALS) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(mUsername) && pieces[1].equals(mPassword)) {
                UserProfile up = new UserProfile(mUsername+"@example.com", "Administrator");
                up.setUserId(mUsername);
                response = new BaseResponse<>();
                response.setSuccess(true);
                response.setResponse(up);
            }
        }

        // Attempt 2 - using external service
        if (response != null && !response.isSuccess()) {
            MultiValueMap<String, String> data = new LinkedMultiValueMap<>(2);
            data.add("username", mUsername);
            data.add("password", mPassword);
            try {
                response = RestClient.INSTANCE.postRequest(loginServiceURL, data, UserProfile.class);
            } catch (Exception e) {
                Log.e(TAG, "Login Service call failed!", e);
            }
        }

        return response;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgress(true);
    }

    @Override
    protected void onPostExecute(final BaseResponse<UserProfile> response) {
        super.onPostExecute(response);

        boolean success = (response != null) && response.isSuccess();
        if (success) {
            UserProfile profile = response.getResponse();
            SessionManager sessionManager = new SessionManager(context);
            sessionManager.createLoginSession(profile);

            // open home activity
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //???
            context.startActivity(intent);

            showProgress(false);
            //activity.finish();

        } else {
            final EditText mPasswordView = activity.findViewById(R.id.password);
            mPasswordView.setError(getString(R.string.error_incorrect_login_attempt));
            mPasswordView.requestFocus();
            showProgress(false);
        }
    }

    @Override
    protected void onCancelled() {
        showProgress(false);
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

        final View mLoginFormView = activity.findViewById(R.id.login_form);
        final View mProgressView = activity.findViewById(R.id.login_progress);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }


    private String getString(int resId) {
        return context.getString(resId);
    }
}
