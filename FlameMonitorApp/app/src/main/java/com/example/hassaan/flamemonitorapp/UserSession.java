package com.example.hassaan.flamemonitorapp;

/**
 * Created by hassaan on 25/03/17.
 */

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserSession {
    SharedPreferences sharedPreferences;
    Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public static final String PREFER_NAME = "FlameMonitor";

    // Shared Preferences Keys
    public static final String IS_USER_LOGGED_IN = "IsUserLoggedIn";
    public static final String KEY_ACCOUNT_ID = "accountID";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";

    public UserSession(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    //Create user login session
    public void createUserLoginSession(String accountID, String phoneNumber){
        editor.putBoolean(IS_USER_LOGGED_IN, true);
        editor.putString(KEY_ACCOUNT_ID, accountID);
        editor.putString(KEY_PHONE_NUMBER,  phoneNumber);

        editor.commit();
    }

    //Get user details
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ACCOUNT_ID, sharedPreferences.getString(KEY_ACCOUNT_ID, null));
        user.put(KEY_PHONE_NUMBER, sharedPreferences.getString(KEY_PHONE_NUMBER, null));

        return user;
    }

    //Logout user by clearing data and redirecting to login page
    public void logoutUser(){
        // Clear all data
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, LoginActivity.class);

        // Closing all the other activities to prevent going back when not logged in
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    //Check login status of application
    public void checkLogin(){
        if(!this.isUserLoggedIn()){
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }

    // Check for login
    public boolean isUserLoggedIn(){
        return sharedPreferences.getBoolean(IS_USER_LOGGED_IN, false);
    }
}
