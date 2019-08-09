package com.pratham.pradigikids.services.auto_sync;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoSyncBootReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        AutoSyncService.start(context);
    }

    public static void enable(Context context) {
        ReceiverUtils.enable(context, AutoSyncBootReceiver.class);
    }

    public static void disable(Context context) {
        ReceiverUtils.disable(context, AutoSyncBootReceiver.class);
    }
}
