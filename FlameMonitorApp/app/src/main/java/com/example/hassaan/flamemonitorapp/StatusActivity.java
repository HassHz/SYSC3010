package com.example.hassaan.flamemonitorapp;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class StatusActivity extends AppCompatActivity {

    Button logoutButton;
    Button streamButton;

    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        userSession = new UserSession(getApplicationContext());

        logoutButton = (Button)findViewById(R.id.logout);
        streamButton = (Button)findViewById(R.id.streamButton);

        logoutButton.setOnClickListener(onLogoutButtonClickListener);
        streamButton.setOnClickListener(onStreamButtonClickListener);

        if(!serviceRunning()){
            Intent service = new Intent(StatusActivity.this, NotificationReceiver.class);
            startService(service);
        }
    }

    private boolean serviceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationReceiver.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    View.OnClickListener onLogoutButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try{
                userSession.logoutUser();
                Intent service = new Intent(StatusActivity.this, NotificationReceiver.class);
                stopService(service);
                finish();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener onStreamButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String mediaURL = "rtsp://172.17.78.98:8080/vid.mp4";
            Intent videoPlayer = new Intent(Intent.ACTION_VIEW, Uri.parse(mediaURL));
            startActivity(videoPlayer);
        }
    };
}
