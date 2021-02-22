package com.hilbing.arkanhelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hilbing.arkanhelp.receivers.BootReceiver;
import com.hilbing.arkanhelp.services.AccelerometerService;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    NavController navController;
    NavHostFragment navHostFragment;
    private AccelerometerService accelerometerService;
    PackageManager packageManager;
    Context context;

    @BindView(R.id.bottomNav)
    BottomNavigationView bottomNavigationView;

    public Context getContext(){
        context = getApplicationContext();
        return context;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //english
        setAppLocale("en");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.main);

        //Getting the Navigation Controller
        navController = Navigation.findNavController(this, R.id.fragment_main);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController);

        accelerometerService = new AccelerometerService(getContext());
        packageManager = context.getPackageManager();

        //Restart service when phone is restarted
        final ComponentName onBootReceiver = new ComponentName(getContext().getPackageName(), BootReceiver.class.getName());
        if(packageManager.getComponentEnabledSetting(onBootReceiver) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
            packageManager.setComponentEnabledSetting(onBootReceiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent serviceIntent = new Intent(this, AccelerometerService.class);
        if(!isMyServiceRunning(accelerometerService.getClass())){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }

        }
    }

    private boolean isMyServiceRunning(Class<? extends AccelerometerService> aClass){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
            if(aClass.getName().equals(serviceInfo.service.getClassName())){
                Log.i(TAG, "isMyServiceRunning: true");
                return true;
            }
        }
        Log.i(TAG, "isMyServiceRunning: false");
        return false;
    }



    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            configuration.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(configuration, displayMetrics);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
               // startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}