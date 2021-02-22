package com.hilbing.arkanhelp.services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

public class CounterService extends Service {

    public static final String TAG = CounterService.class.getSimpleName();
    public static final String TOAST_MESSAGE = "toast_message";

    private NotificationManagerCompat notificationManagerCompat;

    private CountDownTimer mCountDownTimer;
    public static final long START_TIME_IN_MILLIS = 3000;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
