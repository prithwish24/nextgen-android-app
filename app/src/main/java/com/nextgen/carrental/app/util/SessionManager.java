package com.nextgen.carrental.app.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.nextgen.carrental.app.android.LoginActivity;
import com.nextgen.carrental.app.bo.UserProfile;
import com.nextgen.carrental.app.model.User;

import static com.nextgen.carrental.app.constants.GlobalConstants.KEY_EMAIL;
import static com.nextgen.carrental.app.constants.GlobalConstants.KEY_NAME;
import static com.nextgen.carrental.app.constants.GlobalConstants.KEY_SESSIONID;
import static com.nextgen.carrental.app.constants.GlobalConstants.KEY_USERID;
import static com.nextgen.carrental.app.constants.GlobalConstants.SHARED_PREF_NAME;

/**
 * Session Manager
 * @author Prithwish
 */

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
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
        editor.putString(KEY_NAME, userProfile.getFullName());
        editor.putString(KEY_EMAIL, userProfile.getEmailId());
        editor.putString(KEY_USERID, userProfile.getUsername());
        editor.putString(KEY_SESSIONID, sessionId);
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
