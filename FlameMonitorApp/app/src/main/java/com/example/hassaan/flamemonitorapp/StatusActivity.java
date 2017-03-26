package com.example.hassaan.flamemonitorapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class StatusActivity extends AppCompatActivity {

    Button logoutButton;

    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        userSession = new UserSession(getApplicationContext());

        logoutButton = (Button)findViewById(R.id.logout);

        logoutButton.setOnClickListener(onLogoutButtonClickListener);

        System.out.println("Yay, everything works!");
    }

    View.OnClickListener onLogoutButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try{
                userSession.logoutUser();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    };

}
