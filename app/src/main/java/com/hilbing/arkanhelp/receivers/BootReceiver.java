package com.hilbing.arkanhelp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.hilbing.arkanhelp.R;
import com.hilbing.arkanhelp.services.AccelerometerService;

public class BootReceiver extends BroadcastReceiver {

    public static final String TAG = BootReceiver.class.getSimpleName();

    //Restart service when phone is restarted

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null){
            if((intent.getAction().equals("android.intent.action.BOOT_COMPLETED")
            || intent.getAction().equals("android.intetn.action.QUICKBOOT_POWERON")
            || intent.getAction().equals("com.htc.intent.action.QUICKBOOT_POWERON"))){
                Toast.makeText(context, context.getString(R.string.fired_boot_complete), Toast.LENGTH_SHORT).show();
            }
        }
        Log.i(TAG, "onReceive: BootBroadcast BroadcastReceiver +++STARTED+++");
        Log.d(TAG, "onReceive: Boot actionCaught: " + intent.getAction());
        if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
            Log.d(TAG, "onReceive: actionCaught: " + intent.getAction());
        }
        Log.i(TAG, "onReceive: BootBroadcast BroadcastReceiver ---END---");
        Intent serviceIntent = new Intent(context, AccelerometerService.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(serviceIntent);
        }
    }
}
