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
import android.os.PowerManager;
import android.os.Process;
import android.provider.Settings;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by hassaan on 30/03/17.
 */

/**
 * Initializes a background thread that continuously polls for UDP packets and generates Android Notifications
 * when a packet is received.
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

        //Streat a new thread for the network operation so the main thread isn't infinitely frozen
        HandlerThread thread = new HandlerThread("NotificationReceiverThread", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mHandler= new Handler(thread.getLooper());
    }

    /**
     * Post a runnable thread to the thread handler in order to instantiate the thread
     * @param runnable Runnable thread
     */
    public void postRunnable(Runnable runnable) {
        mHandler.post(runnable);
    }

    /**
     * Set running to false when this class is destroyed
     */
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

    /**
     * Runnable UDP Listener thread
     * Continously polls for UDP methods and creates notification if notification alert received
     */
    private Runnable udpListener = new Runnable() {

        @Override
        public void run() {
            String receivedNotification = "notification";
            int zeroOffset = 0;

            try {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket = new DatagramSocket(port);

                byte[] receiveData = new byte[256];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                while (running) {
                    try {
                        socket.receive(receivePacket);
                        String receivedString = new String(receivePacket.getData(), zeroOffset, receivePacket.getLength());
                        if (receivedString.contains(receivedNotification)) {
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

    /**
     * Generates a notification for the application if the application is closed by the user
     * @param context
     * @return void
     */
    private void createNotification(Context context) {
        String notificationTitle = "Flame Monitor";
        String notificationMessage = "A FIRE HAS BEEN DETECTED";
        int screenWakeTime = 15000; //in milliseconds

        Intent statusIntent = new Intent(getApplicationContext(), StatusActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, statusIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        nManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000 })
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setContentIntent(contentIntent);

        //Wake the screen since the notification is urgent
        PowerManager pm = (PowerManager) this
                .getSystemService(Context.POWER_SERVICE);

        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "ALERT");
        wl.acquire(screenWakeTime);

        //Handle notification for different build versions
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