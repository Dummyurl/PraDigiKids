package com.pratham.pradigikids.services.auto_sync;

import android.content.Context;

import com.pratham.pradigikids.custom.shared_preference.FastSave;

public class AutoSyncPreferences {
    static final String NAME = "com.pratham.pradigikids.services.AutoSync.SHARED_PREFS";
    private static final String SEED = "seed";
    private static final String POWER_CONNECTED = "power_connected";
    private static final String LAST_FAILED_TIME_SPAN = "last_failed_time_span";

//    private SharedPreferences prefs;

    AutoSyncPreferences(Context context) {
//        prefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    long getSeed() {
//        return prefs.getLong(SEED, 0);
        return FastSave.getInstance().getLong(SEED, 0);
    }

    void setSeed(long seed) {
//        prefs.edit().putLong(SEED, seed).commit();
        FastSave.getInstance().saveLong(SEED, seed);
    }

    boolean isPowerConnected() {
//        return prefs.getBoolean(POWER_CONNECTED, false);
        return FastSave.getInstance().getBoolean(POWER_CONNECTED, false);
    }

    void setPowerConnected(boolean value) {
//        prefs.edit().putBoolean(POWER_CONNECTED, value).commit();
        FastSave.getInstance().saveBoolean(POWER_CONNECTED, value);
    }

    long getLastFailedTimeSpan(String name) {
//        return prefs.getLong(name + LAST_FAILED_TIME_SPAN, 0);
        return FastSave.getInstance().getLong(name + LAST_FAILED_TIME_SPAN, 0);
    }

    void setLastFailedTimeSpan(String name, long timeSpan) {
//        prefs.edit().putLong(name + LAST_FAILED_TIME_SPAN, timeSpan).commit();
        FastSave.getInstance().saveLong(name + LAST_FAILED_TIME_SPAN, timeSpan);
    }
}
