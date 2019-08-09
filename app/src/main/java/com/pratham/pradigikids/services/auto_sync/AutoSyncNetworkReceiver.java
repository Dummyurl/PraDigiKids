package com.pratham.pradigikids.services.auto_sync;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AutoSyncNetworkReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            disable(context);
            AutoSyncService.networkBack(context);
        }
    }

    static void enable(Context context) {
        ReceiverUtils.enable(context, AutoSyncNetworkReceiver.class);
    }

    static void disable(Context context) {
        ReceiverUtils.disable(context, AutoSyncNetworkReceiver.class);
    }
}
