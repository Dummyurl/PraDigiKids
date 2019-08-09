package com.pratham.pradigikids.services.auto_sync;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Objects;

public class AutoSyncPowerReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean connected = false;
        if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_POWER_CONNECTED)) {
            connected = true;
        } /*else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            connected = false;
        }*/
        startWakefulService(context, AutoSyncService.getPowerChangedIntent(context, connected));
    }

    static void enable(Context context) {
        ReceiverUtils.enable(context, AutoSyncPowerReceiver.class);
    }

    static void disable(Context context) {
        ReceiverUtils.disable(context, AutoSyncPowerReceiver.class);
    }
}
