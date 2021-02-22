package com.hilbing.arkanhelp.services;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.hilbing.arkanhelp.R;

public class App extends Application {

    public static final String CHANNEL_ID = "channelId";
    public static final String CHANNEL_ID_COUNT_DOWN = "channelIdCountDown";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    getResources().getString(R.string.service_channel),
                    NotificationManager.IMPORTANCE_DEFAULT);
            serviceChannel.setDescription(getResources().getString(R.string.fall_service_channel));

            NotificationChannel countDownChannel = new NotificationChannel(
                    CHANNEL_ID_COUNT_DOWN,
                    getResources().getString(R.string.count_down_channel),
                    NotificationManager.IMPORTANCE_HIGH);
            countDownChannel.setDescription(getResources().getString(R.string.count_down_channel));

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
            manager.createNotificationChannel(countDownChannel);
        }
    }
}
