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
    private static final String SHARED_PREF_NAME = "CarRentalPref";
    private static final String IS_LOGGED_IN = "isUserLoggedIn";

    private static final String KEY_NAME = "name";
    private static final String KEY_USERID = "userId";
    private static final String KEY_EMAIL = "email";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.editor.apply();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void createLoginSession(UserProfile userProfile) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(KEY_NAME, userProfile.getFullName());
        editor.putString(KEY_EMAIL, userProfile.getEmailId());
        editor.putString(KEY_USERID, userProfile.getUserId());
        editor.commit();
    }

    public User getLoggedInUser(){
        User user = new User();
        user.setUserId(sharedPreferences.getString(KEY_USERID, ""));
        user.setName(sharedPreferences.getString(KEY_NAME, ""));
        user.setEmail(sharedPreferences.getString(KEY_EMAIL, ""));
        return user;
    }

    public String getLoggedInUserID() {
        return sharedPreferences.getString(KEY_USERID, null);
    }

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
