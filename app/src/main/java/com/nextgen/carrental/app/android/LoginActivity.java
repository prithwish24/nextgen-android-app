package com.nextgen.carrental.app.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.nextgen.carrental.app.R;
import com.nextgen.carrental.app.bo.BaseResponse;
import com.nextgen.carrental.app.bo.UserProfile;
import com.nextgen.carrental.app.constants.GlobalConstants;
import com.nextgen.carrental.app.service.RestClient;
import com.nextgen.carrental.app.service.RestParameter;
import com.nextgen.carrental.app.util.SessionManager;
import com.nextgen.carrental.app.util.Utils;

import org.springframework.core.ParameterizedTypeReference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;


public class LoginActivity extends BaseActivity implements LoaderCallbacks<Cursor> {
    public static final String TAG = LoginActivity.class.getName();
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private CheckBox mRememberChkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_login);

        mUsernameView = findViewById(R.id.username);
        populateAutoComplete();

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mRememberChkBox = findViewById(R.id.remember_me_checkbox);

        Button mEmailSignInButton = findViewById(R.id.user_login_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }*/
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUsernameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final boolean isRememberMe = mRememberChkBox.isChecked();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(username) && !isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();

        } else {
            //UserLoginTask mAuthTask = new UserLoginTask(username, password, this);
            //mAuthTask.execute();
            new LoginTask(this).execute (new UserDTO(username, password, isRememberMe));
        }

    }

    private boolean isUsernameValid(String username) {
        return username.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        //addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    /*private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUsernameView.setAdapter(adapter);
    }*/


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }

    private class UserDTO {
        String username;
        String password;
        boolean rememberMe;
        UserDTO(String username, String password, boolean rememberMe) {
            this.username = username;
            this.password = password;
            this.rememberMe = rememberMe;
        }
    }

    private static class LoginTask extends AsyncTask<UserDTO, Void, BaseResponse<UserProfile>> {
        private static final String[] DUMMY_CREDENTIALS = new String[]{ "admin:admin" };
        private static final String TAG = LoginTask.class.getName();

        private WeakReference<Activity> activityRef;

        LoginTask(Activity mActivity) {
            this.activityRef = new WeakReference<Activity>(mActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected BaseResponse<UserProfile> doInBackground(UserDTO... userDTOS) {
            BaseResponse<UserProfile> response = null;
            UserDTO dto = userDTOS[0];

            // Attempt 1 - using dev credentials
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(dto.username) && pieces[1].equals(dto.password)) {
                    UserProfile up = new UserProfile(dto.username +"@demoapp.com", "Administrator");
                    up.setUsername(dto.username);
                    response = new BaseResponse<>();
                    response.setSuccess(true);
                    response.setResponse(up);
                }
            }

            // Attempt 2 - using external service
            if (response == null) {
                final String loginServiceURL = Utils.getServiceURL (
                        activityRef.get().getApplicationContext(),
                        GlobalConstants.Services.USER_LOGIN);

                RestParameter<UserDTO> parameter = new RestParameter<>();
                parameter.addBody(dto);

                ParameterizedTypeReference<BaseResponse<UserProfile>> typeRef
                        = new ParameterizedTypeReference<BaseResponse<UserProfile>>(){};

                try {
                    response = RestClient.INSTANCE.POST(loginServiceURL, parameter, typeRef);
                } catch (Exception e) {
                    Log.e(TAG, "Login Service call failed!", e);
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(BaseResponse<UserProfile> response) {
            boolean success = (response != null) && response.isSuccess();
            if (success) {
                UserProfile profile = response.getResponse();
                SessionManager sessionManager = new SessionManager(activityRef.get().getApplicationContext());
                sessionManager.createLoginSession(profile, response.getSessionId());

                showProgress(false);

                // open home mActivity
                Intent intent = new Intent(activityRef.get().getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //???
                activityRef.get().getApplicationContext().startActivity(intent);

                activityRef.get().finish();

            } else {
                final TextView mErrorMessage = activityRef.get().findViewById(R.id.login_error_message);
                final String str = activityRef.get().getString(R.string.error_incorrect_login_attempt);
                mErrorMessage.setText(str);
                showProgress(false);
            }

        }

        private void showProgress(final boolean show) {
            final Activity mActivity = activityRef.get();
            final Resources resources = mActivity.getApplicationContext().getResources();
            int shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime);

            final View mLoginFormView = mActivity.findViewById(R.id.login_form);
            final View mProgressView = mActivity.findViewById(R.id.login_progress);

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
    }

}

