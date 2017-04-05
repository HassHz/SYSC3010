package com.example.hassaan.flamemonitorapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.provider.Settings;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by hassaan on 30/03/17.
 */


public class NotificationReceiver extends Service {

    private DatagramSocket socket;
    private int port = 5050;
    private NotificationManager nManager;
    private Handler mHandler;
    private boolean running;

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("NotificationReceiverThread", Process.THREAD_PRIORITY_BACKGROUND);

        thread.start();
        mHandler= new Handler(thread.getLooper());
    }

    public void postRunnable(Runnable runnable) {
        mHandler.post(runnable);
    }

    public void onDestroy() {
        running = false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        running = true;
        postRunnable(udpListener);
        return START_STICKY;
    }

    private Runnable udpListener = new Runnable() {

        @Override
        public void run() {
            try {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket = new DatagramSocket(port);

                byte[] receiveData = new byte[256];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                while (running) {
                    try {
                        socket.receive(receivePacket);
                        String receivedString = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        if (receivedString.contains("notification")) {
                            createNotification(NotificationReceiver.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void createNotification(Context context) {
        Intent statusIntent = new Intent(getApplicationContext(), StatusActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, statusIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        nManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000 })
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentTitle("Flame Monitor")
                .setContentText("A FIRE HAS BEEN DETECTED")
                .setContentIntent(contentIntent);

        if (Build.VERSION.SDK_INT < 16) {
            nManager.notify(1, builder.getNotification());
        } else {
            nManager.notify(1, builder.build());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}