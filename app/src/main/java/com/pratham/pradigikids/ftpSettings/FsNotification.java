/*
Copyright 2011-2013 Pieter Pareit

This file is part of SwiFTP.

SwiFTP is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SwiFTP is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.pratham.pradigikids.ftpSettings;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.ui.dashboard.ActivityMain_;

import java.util.List;

public class FsNotification extends BroadcastReceiver {

    private static final String CHANNEL_ID = "ftp_channel";
    private static final int NOTIFICATION_ID = 7890;

    public static void startingNotification(Context context) {
        Log.d("startingNotification", "start");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!isChannelCreated(context)) {
                createChannel(context);
            }
        }
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nm = (NotificationManager) context.getSystemService(ns);
        // Instantiate a Notification
        int icon = R.mipmap.ic_launcher;
        long when = System.currentTimeMillis();
        CharSequence contentTitle = "FTP Server";
        CharSequence contentText = "server is starting";
        CharSequence tickerText = "wait";
        Notification.Builder nb = new Notification.Builder(context)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(null)
                .setSmallIcon(icon)
                .setTicker(tickerText)
                .setWhen(when)
                .setAutoCancel(true)
                .setOngoing(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nb.setChannelId(CHANNEL_ID);
        }
        Notification notification;
        // go from high to low android version adding extra options
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nb.setVisibility(Notification.VISIBILITY_PUBLIC);
            nb.setCategory(Notification.CATEGORY_SERVICE);
            nb.setPriority(Notification.PRIORITY_MAX);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            nb.setShowWhen(false);
            notification = nb.build();
        } else {
            notification = nb.getNotification();
        }
        FsService.setForeground(NOTIFICATION_ID, notification);
        Log.d("startingNotification", "end");
    }

    private static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String name = "FTP";
            String description = "ftp open state channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.GREEN);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            manager.createNotificationChannel(channel);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            Log.d("create channel", "end");
        }
    }


    private void clearNotification(Context context) {
        Log.d("Clearing", " the notifications");
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nm = (NotificationManager) context.getSystemService(ns);
        nm.cancelAll();
        Log.d("Cleared", "notification");
    }

    private static boolean isChannelCreated(Context context) {
        boolean flag = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            List<NotificationChannel> channelList = manager.getNotificationChannels();
            Log.d("channel list size: ", "" + channelList.size());
            for (int i = 0; i < channelList.size(); i++) {
                NotificationChannel channel = channelList.get(i);
                String id = channel.getId();
                if (id.equals(CHANNEL_ID)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("onReceive broadcast::", "" + intent.getAction());
        switch (intent.getAction()) {
            case FsService.ACTION_UPDATE_NOTIFICATION:
                clearNotification(context);
                setupNotification(context);
                break;
            case FsService.ACTION_STARTED:
                try {
                    setupNotification(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case FsService.ACTION_STOPPED:
                try {
                    clearNotification(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void setupNotification(Context context) {
        Log.d("Setting:", " up the notification");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!isChannelCreated(context)) {
                createChannel(context);
            }
        }
        String iptext = "ftp://192.168.43.1:8080/" /*+ address.getHostAddress() + ":"
                + FsSettings.getPortNumber() + "/"*/;

        // Instantiate a Notification
        int icon = R.mipmap.ic_launcher;
        CharSequence tickerText = String.format(context.getString(R.string.notification_server_starting), iptext);
        long when = System.currentTimeMillis();

        // Define Notification's message and Intent
        CharSequence contentTitle = context.getString(R.string.notification_title);
        CharSequence contentText = String.format(context.getString(R.string.notification_text), iptext);

        Intent notificationIntent = new Intent(PrathamApplication.getInstance(), ActivityMain_.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(PrathamApplication.getInstance(), 0, notificationIntent, 0);

        int stopIcon = android.R.drawable.ic_menu_close_clear_cancel;
        CharSequence stopText = context.getString(R.string.notification_stop_text);
        Intent stopIntent = new Intent(FsService.ACTION_STOP_FTPSERVER);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context, 0,
                stopIntent, PendingIntent.FLAG_ONE_SHOT);

        int preferenceIcon = android.R.drawable.ic_menu_preferences;
        CharSequence preferenceText = context.getString(R.string.notif_settings_text);
        Intent preferenceIntent = new Intent(context, ActivityMain_.class);
        preferenceIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent preferencePendingIntent = PendingIntent.getActivity(context, 0, preferenceIntent, 0);

//        int priority = FsSettings.showNotificationIcon() ? Notification.PRIORITY_DEFAULT
//                : Notification.PRIORITY_MIN;

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(contentIntent)
                .setSmallIcon(icon)
                .setTicker(tickerText)
                .setWhen(when)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setPriority(Notification.PRIORITY_MAX)
//                .addAction(stopIcon, stopText, stopPendingIntent)
//                .addAction(preferenceIcon, preferenceText, preferencePendingIntent)
                .setAutoCancel(true)
                .setShowWhen(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID);
        }
//        // Pass Notification to NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("ftp_channel",
//                    context.getString(R.string.fcm_message),
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
        notificationManager.notify(NOTIFICATION_ID, notification.build());
        Log.d("Notification", " setup done");
    }
}
