package com.example.hassaan.flamemonitorapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class LoginActivity extends AppCompatActivity {

    Button connectButton;
    EditText accountID;
    EditText phoneNumber;

    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userSession = new UserSession(getApplicationContext());

        connectButton = (Button)findViewById(R.id.connect);
        accountID = (EditText)findViewById(R.id.accountID);
        phoneNumber = (EditText)findViewById(R.id.phoneNumber);

        connectButton.setOnClickListener(onConnectButtonClickListener);

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

    public void login() throws Exception {

        String id = accountID.getText().toString();
        String number = phoneNumber.getText().toString();
        System.out.println(number);

        if(id.trim().length() > 0 && number.trim().length() > 0) {
            //TODO: Check if login passes
            if(authenticateLogin(id, number)) {
                userSession.createUserLoginSession(id, number);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Login Failed")
                        .setMessage("Incorrect Account ID or Phone Number")
                        .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        })
                        .setIcon(android.R.drawable.alert_dark_frame)
                        .show();
            }

        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Login Failed")
                    .setMessage("Please enter Account ID and Phone Number")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    })
                    .setIcon(android.R.drawable.alert_dark_frame)
                    .show();
        }

    }

    public boolean authenticateLogin(String accountID, String number) {
        try {
            InetAddress destination = InetAddress.getByName("192.168.0.22");
            DatagramSocket socket = new DatagramSocket();
            byte[] message = (accountID + " " + number).getBytes();
            System.out.println(message);
            DatagramPacket packet = new DatagramPacket(message, message.length, destination, 8080);
            socket.send(packet);
        } catch (UnknownHostException e){
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO: Confirm login
        return true;
    }
}
