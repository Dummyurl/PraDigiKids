package com.pratham.pradigikids.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.util.PD_Constant;
import com.pratham.pradigikids.util.PD_Utility;

import static com.pratham.pradigikids.PrathamApplication.sessionDao;

public class AppKillService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        try {
            PrathamSmartSync.pushUsageToServer(false);
            if (!FastSave.getInstance().getString(PD_Constant.SESSIONID, "").isEmpty()) {
                sessionDao.UpdateToDate(FastSave.getInstance().getString(PD_Constant.SESSIONID, ""), PD_Utility.getCurrentDateTime());
                stopSelf();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}