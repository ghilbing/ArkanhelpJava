package com.hilbing.arkanhelp.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.hilbing.arkanhelp.R;
import com.hilbing.arkanhelp.interfaces.AccelerometerListener;

import java.util.List;

public class AccelerometerManager {

    private static Context mContext = null;
    // accuracy
    private static float umbral = 20.0f;
    private static int interval = 2000;

    private static Sensor sensor;
    private static SensorManager sensorManager;
    private static AccelerometerListener listener;

    //indicates if device supports accelerometer
    private static Boolean supported;
    //indicates if accelerometer is running
    private static boolean running = false;
    //indicates if send messages is active
    private static boolean send_messages_active = false;

    //returns true if manager is listening to orientation changes
    public static boolean isListening(){
        return running;
    }

    public static boolean isSend_messages_active() {
        SharedPreferences sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(mContext);
        send_messages_active = sharedPreferences.getBoolean("activate_messages", false);
        return send_messages_active;
    }

    public static void setSend_messages_active(boolean send_messages_active) {
        AccelerometerManager.send_messages_active = send_messages_active;
    }

    //unregister listeners
    public static void stopListening() {
        running = false;
        try {
            if(sensorManager != null && sensorEventListener != null){
                sensorManager.unregisterListener(sensorEventListener);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean isSupported(Context context){
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

    //Configure the listener for shaking with umbral parameter of minimum acceleration for considering shacking and interval between shake events
    public static void configure(int umbral, int interval){
        AccelerometerManager.umbral = umbral;
        AccelerometerManager.interval = interval;
    }

    //Register listener and start listening
    public static void startListening(AccelerometerListener accelerometerListener){
        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        //Take all sensors
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensors.size()>0){
            sensor = sensors.get(0);
            //Register accelerometer listener
            running = sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
            listener = accelerometerListener;
        }
    }

    //Apply configurations and start listening
    public static void startListening(AccelerometerListener accelerometerListener, int umbral, int interval){
        configure(umbral, interval);
        startListening(accelerometerListener);
    }



    //Configure listener
    private static SensorEventListener sensorEventListener = new SensorEventListener() {
        private long now = 0;
        private long timeDiff = 0;
        private long lastUpdate = 0;
        private long lastShake = 0;
        private float x = 0;
        private float y = 0;
        private float z = 0;
        private float lastX = 0;
        private float lastY = 0;
        private float lastZ = 0;
        private float force = 0;

        public void onAccuracyChanged(Sensor sensor, int accuracy){}
        public void onSensorChanged(SensorEvent event){
            //use timestamp as reference to avoid Accelerometer implementation processing time
            double rootSquare = Math.sqrt(Math.pow(event.values[0],2) + Math.pow(event.values[1],2) + Math.pow(event.values[2],2));
            if(rootSquare < 2.0){
                Toast.makeText(mContext, mContext.getString(R.string.fall_detected), Toast.LENGTH_SHORT).show();
                if(isSend_messages_active()){
                    Toast.makeText(mContext, "Send Messages", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "False Alarm", Toast.LENGTH_SHORT).show();
                }
            }


            /*now = event.timestamp;

            x = event.values[0];
            y = event.values[1];
            z = event.values[2];


            if(lastUpdate == 0){
                lastUpdate = now;
                lastShake = now;
                lastX = x;
                lastY = y;
                lastZ = z;
              //  Toast.makeText(mContext, mContext.getString(R.string.no_motion_detected), Toast.LENGTH_SHORT).show();
            } else {
                timeDiff = now - lastUpdate;
                if(timeDiff > 0){
                    //fall detection
                    double acceleration = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
                    if(acceleration > umbral){
                        lastShake = now;
                        Toast.makeText(mContext, mContext.getString(R.string.fall_detected), Toast.LENGTH_SHORT).show();
                        if(isSend_messages_active()){
                            Toast.makeText(mContext, "Send Messages", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "False Alarm", Toast.LENGTH_SHORT).show();
                        }
                    }


                    //shake detection
                    force = Math.abs(x - lastX);
                    *//*if(Float.compare(force, umbral) > 0){
                        Toast.makeText(mContext, (now-lastShake) + " >= "+ interval, Toast.LENGTH_SHORT).show();
                        if(now - lastShake >= interval){

                            //trigger shake event
                            listener.onShake(force);
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.no_motion_detected), Toast.LENGTH_SHORT).show();
                        }
                        lastShake = now;
                    }
                    lastX = x;
                    lastY = y;
                    lastZ = z;
                    lastUpdate = now;*//*
                }
                else {
                  //  Toast.makeText(mContext, mContext.getString(R.string.no_motion_detected), Toast.LENGTH_SHORT).show();
                }
            }


            //trigger change event
           // listener.onAccelerationChange(x,y,z);*/
        }

    };



}
