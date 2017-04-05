package com.example.hassaan.flamemonitorapp;

import com.michael.easydialog.EasyDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class LoginActivity extends AppCompatActivity {

    Button connectButton;
    EditText accountID;
    EditText phoneNumber;
    ImageButton help;

    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //For network error on real device
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userSession = new UserSession(getApplicationContext());

        connectButton = (Button)findViewById(R.id.connect);
        accountID = (EditText)findViewById(R.id.accountID);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);
        help = (ImageButton)findViewById(R.id.help);

        connectButton.setOnClickListener(onConnectButtonClickListener);
        help.setOnClickListener(onHelpButtonClickListener);
    }

    View.OnClickListener onConnectButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try{
                login();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener onHelpButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new EasyDialog(LoginActivity.this)
                    .setLayoutResourceId(R.layout.tooltip)
                    .setBackgroundColor(Color.BLACK)
                    .setLocationByAttachedView(help)
                    .setGravity(EasyDialog.GRAVITY_TOP)
                    .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 300, 400, 0)
                    .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 300, 0, 400)
                    .setTouchOutsideDismiss(true)
                    .setMatchParent(false)
                    .setMarginLeftAndRight(24, 24)
                    .show();
        }
    };

    public void login() throws Exception {

        String id = accountID.getText().toString();
        String number = phoneNumber.getText().toString();

        if(id.trim().length() > 0 && number.trim().length() > 0) {
            if(authenticateLogin(id, number)) {
                userSession.createUserLoginSession(id, number);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                alertBuilder("Login Failed", "Incorrect Account ID or Phone Number");
            }

        } else {
            alertBuilder("Login Failed", "Please enter Account ID and Phone Number");
        }

    }

    /* Build an alert with a title and message
       @params String title,    The title of the alert
       @params String message,  The alert message

       @return void
     */
    private void alertBuilder(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .setIcon(android.R.drawable.alert_dark_frame)
                .show();
    }

    /* Authenticate a login attempt by communicating login info with the server
       @params String accountID, The ID of the user's account
       @params String number,    The phone number of the user
     */
    public boolean authenticateLogin(String accountID, String number) {
        try {
            InetAddress destination = InetAddress.getByName("172.17.78.98");
            DatagramSocket socket = new DatagramSocket();
            byte[] message = ("get:" + accountID + ":phone").getBytes();
            DatagramPacket packet = new DatagramPacket(message, message.length, destination, 5050);
            socket.send(packet);

            //Login timeouts in 3 seconds
            socket.setSoTimeout(3000);

            //Receive confirmation message
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            while(true) {
                try {
                    socket.receive(receivePacket);
                    String userInfo = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("FROM SERVER:" + userInfo);
                    if(userInfo.contains(number)) {
                        return true;
                    }
                } catch (SocketTimeoutException e) {
                    // timed out
                    System.out.println("Login Failed - Server timed out." + e);
                    return false;
                }
            }

        } catch (UnknownHostException e){
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
