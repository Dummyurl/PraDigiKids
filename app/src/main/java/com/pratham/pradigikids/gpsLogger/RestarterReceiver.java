package com.pratham.pradigikids.gpsLogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.pratham.pradigikids.util.PD_Constant;

public class RestarterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean wasRunning = intent.getBooleanExtra("was_running", false);
        Intent serviceIntent = new Intent(context, GpsLoggingService.class);
        if (wasRunning) {
            serviceIntent.putExtra(PD_Constant.GPS_LOGGER_IMMEDIATE_START, true);
            ContextCompat.startForegroundService(context, serviceIntent);
        } else {
            serviceIntent.putExtra(PD_Constant.GPS_LOGGER_IMMEDIATE_STOP, true);
            ContextCompat.startForegroundService(context, serviceIntent);
        }

    }
}
