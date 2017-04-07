package com.example.hassaan.flamemonitorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private UserSession userSession;
    private boolean debugging = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create a new instance of sharedPreferences usersession to see if a user is logged in
        userSession = new UserSession(getApplicationContext());
        if (debugging) System.out.println(userSession.isUserLoggedIn());

        //Launch appropriate activity whether user is logged in or not
        if(!userSession.isUserLoggedIn()){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
