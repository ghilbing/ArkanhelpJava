package com.hilbing.arkanhelp.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.hilbing.arkanhelp.MainActivity;
import com.hilbing.arkanhelp.R;
import com.hilbing.arkanhelp.interfaces.AccelerometerListener;
import com.hilbing.arkanhelp.receivers.SensorRestarterBroadcastReceiver;

import java.util.List;

import static com.hilbing.arkanhelp.services.App.CHANNEL_ID;

public class AccelerometerService extends Service implements SensorEventListener {

    public static final String TAG = AccelerometerService.class.getSimpleName();
    private Context mContext;

    private SensorManager sensorManager;
    private Sensor mAccelerometer;
    //indicates if device supports accelerometer
    private static Boolean supported;
    //indicates if send messages is active
    private static boolean send_messages_active = false;


    public AccelerometerService(Context context){
        super();
        mContext = context;
        Log.i(TAG, "AccelerometerService: running");
    }

    public AccelerometerService(){}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);



        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.arkanhelp))
                .setContentText(getResources().getString(R.string.service_running))
                .setSmallIcon(R.drawable.ic_help)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isSupported(mContext);

    }

    public boolean isSend_messages_active() {
        SharedPreferences sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        send_messages_active = sharedPreferences.getBoolean("activate_messages", false);
        return send_messages_active;
    }

    public boolean isSupported(Context context){
        mContext = context;
        if(supported == null){
            if(mContext != null){
                sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
                //get all sensors in device
                List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
                supported = new Boolean(sensors.size()>0);
            } else {
                supported = Boolean.FALSE;
            }
        }
        return supported;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent(this, SensorRestarterBroadcastReceiver.class));
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(getApplicationContext(), AccelerometerService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(mAccelerometer != null){
            sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, getResources().getString(R.string.this_device_do_not_support_accelerometer), Toast.LENGTH_SHORT).show();
        }

        double rootSquare = Math.sqrt(Math.pow(sensorEvent.values[0],2) + Math.pow(sensorEvent.values[1],2) + Math.pow(sensorEvent.values[2],2));
        if(rootSquare < 2.0){
            Toast.makeText(this, getResources().getString(R.string.fall_detected), Toast.LENGTH_SHORT).show();
            if(isSend_messages_active()){
                Toast.makeText(getApplicationContext(), "Send Messages", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "False Alarm", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
