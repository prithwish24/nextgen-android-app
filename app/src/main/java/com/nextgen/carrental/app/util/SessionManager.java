package com.nextgen.carrental.app.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.nextgen.carrental.app.android.LoginActivity;
import com.nextgen.carrental.app.bo.UserProfile;
import com.nextgen.carrental.app.model.User;

/**
 * Session Manager
 * @author Prithwish
 */

public class SessionManager {
    public static final String SESSION_KEY_FIRSTNAME = "firstname";
    public static final String SESSION_KEY_LASTNAME = "lastname";
    public static final String SESSION_KEY_USERID = "userId";
    public static final String SESSION_KEY_EMAIL = "email";
    public static final String SESSION_KEY_ID = "sessionId";
    public static final String SESSION_KEY_CARPREF = "carPreference";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    public static final String SHARED_PREF_NAME = "CarRentalPref";
    private static final String IS_LOGGED_IN = "isUserLoggedIn";

    public SessionManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.editor.apply();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void createLoginSession(UserProfile userProfile,String sessionId) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(SESSION_KEY_FIRSTNAME, userProfile.getFirstName());
        editor.putString(SESSION_KEY_LASTNAME, userProfile.getLastName());
        editor.putString(SESSION_KEY_EMAIL, userProfile.getEmailId());
        editor.putString(SESSION_KEY_USERID, userProfile.getUsername());
        editor.putString(SESSION_KEY_CARPREF, userProfile.getCarTypePref());
        editor.putString(SESSION_KEY_ID, sessionId);
        editor.commit();
    }

    @Deprecated
    public User getLoggedInUser(){
        User user = new User();
        user.setUserId(sharedPreferences.getString(SESSION_KEY_USERID, ""));
        user.setName(sharedPreferences.getString(SESSION_KEY_FIRSTNAME, ""));
        user.setEmail(sharedPreferences.getString(SESSION_KEY_EMAIL, ""));
        return user;
    }

    public String getLoggedInUserGivenName() {
        return sharedPreferences.getString(SESSION_KEY_FIRSTNAME, null);
    }

    public String getLoggedInUserLastName() {
        return sharedPreferences.getString(SESSION_KEY_LASTNAME, null);
    }

    public String getLoggedInUserEmail() {
        return sharedPreferences.getString(SESSION_KEY_EMAIL, null);
    }

    public String getLoggedInUserCarPref() {
        return sharedPreferences.getString(SESSION_KEY_CARPREF, null);
    }

    public String getLoggedInUserID() {
        return sharedPreferences.getString(SESSION_KEY_USERID, null);
    }

    public String getLoggedInSessionID () {
        return sharedPreferences.getString(SESSION_KEY_ID, null);
    }

    @Deprecated
    public String getData(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }

}
