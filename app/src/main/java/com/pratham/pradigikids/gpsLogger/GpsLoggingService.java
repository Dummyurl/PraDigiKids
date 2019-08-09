/*
 * Copyright (C) 2016 mendhak
 *
 * This file is part of GPSLogger for Android.
 *
 * GPSLogger for Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * GPSLogger for Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GPSLogger for Android.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pratham.pradigikids.gpsLogger;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.pratham.pradigikids.R;
import com.pratham.pradigikids.models.EventMessage;
import com.pratham.pradigikids.ui.splash.ActivitySplash_;
import com.pratham.pradigikids.util.PD_Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class GpsLoggingService extends Service {
    private static final String TAG = GpsLoggingService.class.getSimpleName();
    //    private static NotificationManager notificationManager;
//    private static int NOTIFICATION_ID = 8675309;
    private final IBinder binder = new GpsLoggingBinder();
    protected LocationManager gpsLocationManager;
    //    private NotificationCompat.Builder nfc;
    // ---------------------------------------------------
    // Helpers and managers
    // ---------------------------------------------------
    private final PreferenceHelper preferenceHelper = PreferenceHelper.getInstance();
    private final Session session = Session.getInstance();
    private final Handler handler = new Handler();
    private AlarmManager nextPointAlarmManager;
    private LocationManager passiveLocationManager;
    private LocationManager towerLocationManager;
    private GeneralLocationListener gpsLocationListener;
    private GeneralLocationListener towerLocationListener;
    private GeneralLocationListener passiveLocationListener;
    private Intent alarmIntent;
    // ---------------------------------------------------
    private final Runnable stopManagerRunnable = this::stopManagerAndResetAlarm;
    private PendingIntent activityRecognitionPendingIntent;

    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }

    @Override
    public void onCreate() {

        nextPointAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        registerEventBus();
    }

    private void requestActivityRecognitionUpdates() {
        if (preferenceHelper.shouldNotLogIfUserIsStill()) {
            Log.d(TAG, "requestActivityRecognitionUpdates: ");
            Intent intent = new Intent(getApplicationContext(), GpsLoggingService.class);
            activityRecognitionPendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            ActivityRecognitionClient arClient = ActivityRecognition.getClient(getApplicationContext());
            arClient.requestActivityUpdates(preferenceHelper.getMinimumLoggingInterval() * 1000, activityRecognitionPendingIntent);
        }
    }

    private void stopActivityRecognitionUpdates() {
        try {
            if (activityRecognitionPendingIntent != null) {
                Log.d(TAG, "stopActivityRecognitionUpdates: ");
                ActivityRecognitionClient arClient = ActivityRecognition.getClient(getApplicationContext());
                arClient.removeActivityUpdates(activityRecognitionPendingIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    private void unregisterEventBus() {
        try {
            EventBus.getDefault().unregister(this);
        } catch (Throwable t) {
            t.printStackTrace();
            //this may crash if registration did not go through. just be safe
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        startForeground(NOTIFICATION_ID, getNotification());
        handleIntent(intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterEventBus();
//        removeNotification();
        super.onDestroy();

        if (session.isStarted()) {
            Intent broadcastIntent = new Intent(getApplicationContext(), RestarterReceiver.class);
            broadcastIntent.putExtra("was_running", true);
            sendBroadcast(broadcastIntent);
        }
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory: ");
        super.onLowMemory();
    }

    private void handleIntent(Intent intent) {
        ActivityRecognitionResult arr = ActivityRecognitionResult.extractResult(intent);
        if (arr != null) {
            EventBus.getDefault().post(new ServiceEvents.ActivityRecognitionEvent(arr));
            return;
        }

        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if (!Systems.locationPermissionsGranted(this)) {
                    Log.d(TAG, "User has not granted permission to access location services. Will not continue!");
                    stopLogging();
                    stopSelf();
                    return;
                }

                boolean needToStartGpsManager = false;

                if (bundle.getBoolean(PD_Constant.GPS_LOGGER_IMMEDIATE_START)) {
                    EventBus.getDefault().post(new CommandEvents.RequestStartStop(true));
                }

                if (bundle.getBoolean(PD_Constant.GPS_LOGGER_IMMEDIATE_STOP)) {
                    EventBus.getDefault().post(new CommandEvents.RequestStartStop(false));
                }

                if (bundle.getBoolean(PD_Constant.GPS_LOGGER_GET_STATUS)) {
                    EventBus.getDefault().post(new CommandEvents.GetStatus());
                }
                if (bundle.getBoolean(PD_Constant.GPS_LOGGER_GET_NEXT_POINT)) {
                    needToStartGpsManager = true;
                }
                if (bundle.getString(PD_Constant.GPS_LOGGER_SET_DESCRIPTION) != null) {
                    EventBus.getDefault().post(new CommandEvents.Annotate(bundle.getString(PD_Constant.GPS_LOGGER_SET_DESCRIPTION)));
                }
                if (bundle.get(PD_Constant.GPS_LOGGER_PREFER_CELLTOWER) != null) {
                    boolean preferCellTower = bundle.getBoolean(PD_Constant.GPS_LOGGER_PREFER_CELLTOWER);

                    if (preferCellTower) {
                        preferenceHelper.setChosenListeners(1);
                    } else {
                        preferenceHelper.setChosenListeners(0);
                    }

                    needToStartGpsManager = true;
                }
                if (bundle.get(PD_Constant.GPS_LOGGER_TIME_BEFORE_LOGGING) != null) {
                    int timeBeforeLogging = bundle.getInt(PD_Constant.GPS_LOGGER_TIME_BEFORE_LOGGING);
                    preferenceHelper.setMinimumLoggingInterval(timeBeforeLogging);
                    needToStartGpsManager = true;
                }

                if (bundle.get(PD_Constant.GPS_LOGGER_DISTANCE_BEFORE_LOGGING) != null) {
                    int distanceBeforeLogging = bundle.getInt(PD_Constant.GPS_LOGGER_DISTANCE_BEFORE_LOGGING);
                    preferenceHelper.setMinimumDistanceInMeters(distanceBeforeLogging);
                    needToStartGpsManager = true;
                }

                if (bundle.get(PD_Constant.GPS_LOGGER_GPS_ON_BETWEEN_FIX) != null) {
                    boolean keepBetweenFix = bundle.getBoolean(PD_Constant.GPS_LOGGER_GPS_ON_BETWEEN_FIX);
                    preferenceHelper.setShouldKeepGPSOnBetweenFixes(keepBetweenFix);
                    needToStartGpsManager = true;
                }

                if (bundle.get(PD_Constant.GPS_LOGGER_RETRY_TIME) != null) {
                    int retryTime = bundle.getInt(PD_Constant.GPS_LOGGER_RETRY_TIME);
                    preferenceHelper.setLoggingRetryPeriod(retryTime);
                    needToStartGpsManager = true;
                }

                if (bundle.get(PD_Constant.GPS_LOGGER_ABSOLUTE_TIMEOUT) != null) {
                    int absoluteTimeout = bundle.getInt(PD_Constant.GPS_LOGGER_ABSOLUTE_TIMEOUT);
                    preferenceHelper.setAbsoluteTimeoutForAcquiringPosition(absoluteTimeout);
                    needToStartGpsManager = true;
                }

                if (bundle.get(PD_Constant.GPS_LOGGER_LOG_ONCE) != null) {
                    boolean logOnceIntent = bundle.getBoolean(PD_Constant.GPS_LOGGER_LOG_ONCE);
                    needToStartGpsManager = false;
                    logOnce();
                }
                try {
                    if (bundle.get(Intent.EXTRA_ALARM_COUNT) != "0") {
                        needToStartGpsManager = true;
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                    needToStartGpsManager = false;
                }
                if (needToStartGpsManager && session.isStarted()) {
                    startGpsManager();
                }
            }
        } else {
            // A null intent is passed in if the service has been killed and restarted.
            if (session.isStarted()) {
                startLogging();
            }
        }
    }

    /**
     * Sets up the auto email timers based on user preferences.
     *
     * @TargetApi(23) public void setupAutoSendTimers() {
     * if (preferenceHelper.isAutoSendEnabled() && session.getAutoSendDelay() > 0) {
     * long triggerTime = System.currentTimeMillis() + (long) (session.getAutoSendDelay() * 60 * 1000);
     * <p>
     * alarmIntent = new Intent(this, AlarmReceiver.class);
     * cancelAlarm();
     * <p>
     * PendingIntent sender = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
     * AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
     * if (Systems.isDozing(this)) {
     * am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, sender);
     * } else {
     * am.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
     * }
     * LOG.debug("Autosend alarm has been set");
     * <p>
     * } else {
     * if (alarmIntent != null) {
     * LOG.debug("alarmIntent was null, canceling alarm");
     * cancelAlarm();
     * }
     * }
     * }
     */


    private void logOnce() {
        session.setSinglePointMode(true);
        if (session.isStarted()) {
            startGpsManager();
        } else {
            startLogging();
        }
    }

    /**
     * Calls the Auto Senders which process the files and send it.
     * private void autoSendLogFile(@Nullable String formattedFileName) {
     * <p>
     * if (!Strings.isNullOrEmpty(formattedFileName) || !Strings.isNullOrEmpty(Strings.getFormattedFileName())) {
     * String fileToSend = Strings.isNullOrEmpty(formattedFileName) ? Strings.getFormattedFileName() : formattedFileName;
     * FileSenderFactory.autoSendFiles(fileToSend);
     * setupAutoSendTimers();
     * }
     * }
     * <p>
     * private void resetAutoSendTimersIfNecessary() {
     * if (session.getAutoSendDelay() != preferenceHelper.getAutoSendInterval()) {
     * session.setAutoSendDelay(preferenceHelper.getAutoSendInterval());
     * setupAutoSendTimers();
     * }
     * }
     */

    private void cancelAlarm() {
        if (alarmIntent != null) {
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            PendingIntent sender = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(sender);
        }
    }

    /**
     * Resets the form, resets file name if required, reobtains preferences
     */
    private void startLogging() {
        session.setAddNewTrackSegment(true);
//        try {
//            startForeground(NOTIFICATION_ID, getNotification());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        session.setStarted(true);
//        resetAutoSendTimersIfNecessary();
//        showNotification();
//        setupAutoSendTimers();
//        resetCurrentFileName(true);
        notifyClientsStarted(true);
        startPassiveManager();
        startGpsManager();
        requestActivityRecognitionUpdates();
    }

    private void notifyByBroadcast(boolean loggingStarted) {
        String event = (loggingStarted) ? "started" : "stopped";
        Intent sendIntent = new Intent();
        sendIntent.setAction("com.pratham.pradigikids.EVENT");
        sendIntent.putExtra("gpsloggerevent", event);
        sendIntent.putExtra("filename", session.getCurrentFormattedFileName());
        sendIntent.putExtra("startedtimestamp", session.getStartTimeStamp());
        sendBroadcast(sendIntent);
    }

    /**
     * Informs main activity and broadcast listeners whether logging has started/stopped
     */
    private void notifyClientsStarted(boolean started) {
        notifyByBroadcast(started);
        EventBus.getDefault().post(new ServiceEvents.LoggingStatus(started));
    }

    /**
     * Notify status of logger
     */
    private void notifyStatus(boolean started) {
        notifyByBroadcast(started);
    }

    /**
     * Stops logging, removes notification, stops GPS manager, stops email timer
     */
    private void stopLogging() {
        session.setAddNewTrackSegment(true);
        session.setTotalTravelled(0);
        session.setPreviousLocationInfo(null);
        session.setStarted(false);
        session.setUserStillSinceTimeStamp(0);
        session.setLatestTimeStamp(0);
        stopAbsoluteTimer();
        // Email log file before setting location info to null
//        autoSendLogFileOnStop();
        cancelAlarm();
        session.setCurrentLocationInfo(null);
        session.setSinglePointMode(false);
        stopForeground(true);

        removeNotification();
        stopAlarm();
        stopGpsManager();
        stopPassiveManager();
        stopActivityRecognitionUpdates();
        notifyClientsStarted(false);
        session.setCurrentFileName("");
        session.setCurrentFormattedFileName("");
        stopSelf();
    }

    /**
     * Hides the notification icon in the status bar if it's visible.
     */
    private void removeNotification() {
//        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.cancelAll();
    }

    /**
     * Shows a notification icon in the status bar for GPS Logger
     */
    private Notification getNotification() {

        Intent stopLoggingIntent = new Intent(this, GpsLoggingService.class);
        stopLoggingIntent.setAction("NotificationButton_STOP");
        stopLoggingIntent.putExtra(PD_Constant.GPS_LOGGER_IMMEDIATE_STOP, true);
        PendingIntent piStop = PendingIntent.getService(this, 0, stopLoggingIntent, 0);

        Intent annotateIntent = new Intent(this, ActivitySplash_.class);
        annotateIntent.setAction("com.pratham.pradigikids.NOTIFICATION_BUTTON");
        PendingIntent piAnnotate = PendingIntent.getActivity(this, 0, annotateIntent, 0);

        // What happens when the notification item is clicked
        Intent contentIntent = new Intent(this, ActivitySplash_.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(contentIntent);

        PendingIntent pending = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        CharSequence contentTitle = "GPS Logger is still running";
        CharSequence contentText = getString(R.string.app_name);
        long notificationTime = System.currentTimeMillis();

        if (session.hasValidLocation()) {
            contentTitle = Strings.getFormattedLatitude(session.getCurrentLatitude()) + ", "
                    + Strings.getFormattedLongitude(session.getCurrentLongitude());

            contentText = Html.fromHtml("<b>" + "Altitude" + "</b> " + Strings.getDistanceDisplay(this, session.getCurrentLocationInfo().getAltitude(), /*preferenceHelper.shouldDisplayImperialUnits()*/false, false)
                    + "  "
                    + "<b>" + "Duration" + "</b> " + Strings.getDescriptiveDurationString((int) (System.currentTimeMillis() - session.getStartTimeStamp()) / 1000, this)
                    + "  "
                    + "<b>" + "Accuracy" + "</b> " + Strings.getDistanceDisplay(this, session.getCurrentLocationInfo().getAccuracy(), /*preferenceHelper.shouldDisplayImperialUnits()*/false, true));

            notificationTime = session.getCurrentLocationInfo().getTime();
        }

        /*if (nfc == null) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationChannel channel = new NotificationChannel("gpslogger", getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableLights(false);
                channel.enableVibration(false);
                channel.setSound(null, null);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

                channel.setShowBadge(true);
                manager.createNotificationChannel(channel);

            }

            nfc = new NotificationCompat.Builder(getApplicationContext(), "gpslogger")
                    .setSmallIcon(R.drawable.ic_app_logo_)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_app_logo_))
                    .setPriority(preferenceHelper.shouldHideNotificationFromStatusBar() ? NotificationCompat.PRIORITY_MIN : NotificationCompat.PRIORITY_LOW)
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .setVisibility(NotificationCompat.VISIBILITY_SECRET) //This hides the notification from lock screen
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText).setBigContentTitle(contentTitle))
                    .setOngoing(true)
                    .setContentIntent(pending);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                nfc.setPriority(NotificationCompat.PRIORITY_LOW);
            }

            if (!preferenceHelper.shouldHideNotificationButtons()) {
                nfc.addAction(R.drawable.dialog_close, "Annotate", piAnnotate)
                        .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", piStop);
            }
        }
        nfc.setContentTitle(contentTitle);
        nfc.setContentText(contentText);
        nfc.setStyle(new NotificationCompat.BigTextStyle().bigText(contentText).setBigContentTitle(contentTitle));
        nfc.setWhen(notificationTime);*/

        //notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //notificationManager.notify(NOTIFICATION_ID, nfc.build());
        return /*nfc.build()*/null;
    }

    private void showNotification() {
        Notification notif = getNotification();
//        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(NOTIFICATION_ID, notif);
    }

    @SuppressLint("MissingPermission")
    private void startPassiveManager() {
        if (preferenceHelper.shouldLogPassiveLocations()) {
            if (passiveLocationListener == null) {
                passiveLocationListener = new GeneralLocationListener(this, PD_Constant.GPS_PASSIVE);
            }
            passiveLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            passiveLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 0, passiveLocationListener);
        }
    }

    /**
     * Starts the location manager. There are two location managers - GPS and
     * Cell Tower. This code determines which manager to request updates from
     * based on user preference and whichever is enabled. If GPS is enabled on
     * the phone, that is used. But if the user has also specified that they
     * prefer cell towers, then cell towers are used. If neither is enabled,
     * then nothing is requested.
     */
    @SuppressWarnings("ResourceType")
    private void startGpsManager() {
        //If the user has been still for more than the minimum seconds
        if (userHasBeenStillForTooLong()) {
            setAlarmForNextPoint();
            return;
        }

        if (gpsLocationListener == null) {
            gpsLocationListener = new GeneralLocationListener(this, "GPS");
        }

        if (towerLocationListener == null) {
            towerLocationListener = new GeneralLocationListener(this, "CELL");
        }

        gpsLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        towerLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        checkTowerAndGpsStatus();

        if (session.isGpsEnabled() && preferenceHelper.getChosenListeners().contains(LocationManager.GPS_PROVIDER)) {
            // gps satellite based
            gpsLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, gpsLocationListener);
            gpsLocationManager.addGpsStatusListener(gpsLocationListener);
            gpsLocationManager.addNmeaListener(gpsLocationListener);

            session.setUsingGps(true);
            startAbsoluteTimer();
        }

        if (session.isTowerEnabled() && (preferenceHelper.getChosenListeners().contains(LocationManager.NETWORK_PROVIDER) || !session.isGpsEnabled())) {
            session.setUsingGps(false);
            // Cell tower and wifi based
            towerLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, towerLocationListener);

            startAbsoluteTimer();
        }

        if (!session.isTowerEnabled() && !session.isGpsEnabled()) {
            session.setUsingGps(false);
            stopLogging();
            setLocationServiceUnavailable();
            return;
        }

        EventBus.getDefault().post(new ServiceEvents.WaitingForLocation(true));
        session.setWaitingForLocation(true);
    }

    private boolean userHasBeenStillForTooLong() {
        return !session.hasDescription() && !session.isSinglePointMode() &&
                (session.getUserStillSinceTimeStamp() > 0 && (System.currentTimeMillis() - session.getUserStillSinceTimeStamp()) > (preferenceHelper.getMinimumLoggingInterval() * 1000));
    }

    private void startAbsoluteTimer() {
        if (preferenceHelper.getAbsoluteTimeoutForAcquiringPosition() >= 1) {
            handler.postDelayed(stopManagerRunnable, preferenceHelper.getAbsoluteTimeoutForAcquiringPosition() * 1000);
        }
    }

    private void stopAbsoluteTimer() {
        handler.removeCallbacks(stopManagerRunnable);
    }

    /**
     * This method is called periodically to determine whether the cell tower /
     * gps providers have been enabled, and sets class level variables to those
     * values.
     */
    private void checkTowerAndGpsStatus() {
        session.setTowerEnabled(towerLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
        session.setGpsEnabled(gpsLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    /**
     * Stops the location managers
     */
    @SuppressWarnings("ResourceType")
    private void stopGpsManager() {

        if (towerLocationListener != null) {
            towerLocationManager.removeUpdates(towerLocationListener);
        }

        if (gpsLocationListener != null) {
            gpsLocationManager.removeUpdates(gpsLocationListener);
            gpsLocationManager.removeGpsStatusListener(gpsLocationListener);
        }

        session.setWaitingForLocation(false);
        EventBus.getDefault().post(new ServiceEvents.WaitingForLocation(false));

    }

    @SuppressWarnings("ResourceType")
    private void stopPassiveManager() {
        if (passiveLocationManager != null) {
            passiveLocationManager.removeUpdates(passiveLocationListener);
        }
    }

    private void setLocationServiceUnavailable() {
        EventBus.getDefault().post(new ServiceEvents.LocationServicesUnavailable());
    }

    /**
     * Stops location manager, then starts it.
     */
    void restartGpsManagers() {
        stopGpsManager();
        startGpsManager();
    }

    /**
     * This event is raised when the GeneralLocationListener has a new location.
     * This method in turn updates notification, writes to file, reobtains
     * preferences, notifies main service client and resets location managers.
     *
     * @param loc Location object
     */
    void onLocationChanged(Location loc) {
        Log.d(TAG, "onLocationChanged: lat:" + loc.getLatitude() + "Long:" + loc.getLongitude());
        EventMessage message = new EventMessage();
        message.setMessage(PD_Constant.LOCATION_CHANGED);
        message.setLocation(loc);
        EventBus.getDefault().post(message);
        if (!session.isStarted()) {
            stopLogging();
            return;
        }
        boolean isPassiveLocation = loc.getExtras().getBoolean(PD_Constant.GPS_PASSIVE);
        long currentTimeStamp = System.currentTimeMillis();

        // Don't log a point until the user-defined time has elapsed
        // However, if user has set an annotation, just log the point, disregard any filters
        if (!isPassiveLocation && !session.hasDescription() && !session.isSinglePointMode() && (currentTimeStamp - session.getLatestTimeStamp()) < (preferenceHelper.getMinimumLoggingInterval() * 1000)) {
            return;
        }

        //Don't log a point if user has been still
        // However, if user has set an annotation, just log the point, disregard any filters
        if (userHasBeenStillForTooLong()) {
            return;
        }

        if (!isPassiveLocation && !isFromValidListener(loc)) {
            return;
        }

        //Check if a ridiculous distance has been travelled since previous point - could be a bad GPS jump
        if (session.getCurrentLocationInfo() != null) {
            double distanceTravelled = Maths.calculateDistance(loc.getLatitude(), loc.getLongitude(), session.getCurrentLocationInfo().getLatitude(), session.getCurrentLocationInfo().getLongitude());
            long timeDifference = (int) Math.abs(loc.getTime() - session.getCurrentLocationInfo().getTime()) / 1000;
            if (timeDifference > 0 && (distanceTravelled / timeDifference) > 357) { //357 m/s ~=  1285 km/h
                Log.d(TAG, "onLocationChanged: " + "Very large jump detected - %d meters in %d sec - discarding point" + (long) distanceTravelled);
                return;
            }
        }

        // Don't do anything until the user-defined accuracy is reached
        // However, if user has set an annotation, just log the point, disregard any filters
        if (!isPassiveLocation && !session.hasDescription() && preferenceHelper.getMinimumAccuracy() > 0) {
            if (!loc.hasAccuracy() || loc.getAccuracy() == 0) {
                return;
            }
            //Don't apply the retry interval to passive locations
            if (preferenceHelper.getMinimumAccuracy() < Math.abs(loc.getAccuracy())) {
                if (session.getFirstRetryTimeStamp() == 0) {
                    session.setFirstRetryTimeStamp(System.currentTimeMillis());
                }
                if (currentTimeStamp - session.getFirstRetryTimeStamp() <= preferenceHelper.getLoggingRetryPeriod() * 1000) {
                    Log.d("onLocationChanged: ", "Only accuracy of " + loc.getAccuracy() + " m. Point discarded. Inaccurate_point_discarded");
                    //return and keep trying
                    return;
                }

                if (currentTimeStamp - session.getFirstRetryTimeStamp() > preferenceHelper.getLoggingRetryPeriod() * 1000) {
                    //Give up for now
                    stopManagerAndResetAlarm();

                    //reset timestamp for next time.
                    session.setFirstRetryTimeStamp(0);
                    return;
                }
                //Success, reset timestamp for next time.
                session.setFirstRetryTimeStamp(0);
            }
        }

        //Don't do anything until the user-defined distance has been traversed
        // However, if user has set an annotation, just log the point, disregard any filters
        if (!session.hasDescription() && !session.isSinglePointMode() && preferenceHelper.getMinimumDistanceInterval() > 0 && session.hasValidLocation()) {

            double distanceTraveled = Maths.calculateDistance(loc.getLatitude(), loc.getLongitude(),
                    session.getCurrentLatitude(), session.getCurrentLongitude());

            if (preferenceHelper.getMinimumDistanceInterval() > distanceTraveled) {
                stopManagerAndResetAlarm();
                return;
            }
        }


        loc = Locations.getLocationWithAdjustedAltitude(loc, preferenceHelper);
        session.setLatestTimeStamp(System.currentTimeMillis());
        session.setFirstRetryTimeStamp(0);
        session.setCurrentLocationInfo(loc);
        setDistanceTraveled(loc);
//        showNotification();
        stopManagerAndResetAlarm();

        EventBus.getDefault().post(new ServiceEvents.LocationUpdate(loc));

        if (session.isSinglePointMode()) {
            stopLogging();
        }
    }

    private boolean isFromValidListener(Location loc) {

        if (!preferenceHelper.getChosenListeners().contains(LocationManager.GPS_PROVIDER) && !preferenceHelper.getChosenListeners().contains(LocationManager.NETWORK_PROVIDER)) {
            return true;
        }

        if (!preferenceHelper.getChosenListeners().contains(LocationManager.NETWORK_PROVIDER)) {
            return loc.getProvider().equalsIgnoreCase(LocationManager.GPS_PROVIDER);
        }

        if (!preferenceHelper.getChosenListeners().contains(LocationManager.GPS_PROVIDER)) {
            return !loc.getProvider().equalsIgnoreCase(LocationManager.GPS_PROVIDER);
        }

        return true;
    }

    private void setDistanceTraveled(Location loc) {
        // Distance
        if (session.getPreviousLocationInfo() == null) {
            session.setPreviousLocationInfo(loc);
        }
        // Calculate this location and the previous location location and add to the current running total distance.
        // NOTE: Should be used in conjunction with 'distance required before logging' for more realistic values.
        double distance = Maths.calculateDistance(
                session.getPreviousLatitude(),
                session.getPreviousLongitude(),
                loc.getLatitude(),
                loc.getLongitude());
        session.setPreviousLocationInfo(loc);
        session.setTotalTravelled(session.getTotalTravelled() + distance);
    }

    protected void stopManagerAndResetAlarm() {
        if (!preferenceHelper.shouldKeepGPSOnBetweenFixes()) {
            stopGpsManager();
        }

        stopAbsoluteTimer();
        setAlarmForNextPoint();
    }


    private void stopAlarm() {
        Intent i = new Intent(this, GpsLoggingService.class);
        i.putExtra(PD_Constant.GPS_LOGGER_GET_NEXT_POINT, true);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        nextPointAlarmManager.cancel(pi);
    }

    @TargetApi(23)
    private void setAlarmForNextPoint() {

        Intent i = new Intent(this, GpsLoggingService.class);
        i.putExtra(PD_Constant.GPS_LOGGER_GET_NEXT_POINT, true);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        nextPointAlarmManager.cancel(pi);

        if (Systems.isDozing(this)) {
            //Only invoked once per 15 minutes in doze mode
            nextPointAlarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + preferenceHelper.getMinimumLoggingInterval() * 1000, pi);
        } else {
            nextPointAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + preferenceHelper.getMinimumLoggingInterval() * 1000, pi);
        }
    }

    /**
     * Informs the main service client of the number of visible satellites.
     *
     * @param count Number of Satellites
     */
    void setSatelliteInfo(int count) {
        session.setVisibleSatelliteCount(count);
        EventBus.getDefault().post(new ServiceEvents.SatellitesVisible(count));
    }

    @Subscribe
    public void onEvent(CommandEvents.RequestToggle requestToggle) {
        if (session.isStarted()) {
            stopLogging();
        } else {
            startLogging();
        }
    }

    @Subscribe
    public void onEvent(CommandEvents.RequestStartStop startStop) {
        if (startStop.start) {
            startLogging();
        } else {
            stopLogging();
        }

        EventBus.getDefault().removeStickyEvent(CommandEvents.RequestStartStop.class);
    }

    @Subscribe
    public void onEvent(CommandEvents.GetStatus getStatus) {
        CommandEvents.GetStatus statusEvent = EventBus.getDefault().removeStickyEvent(CommandEvents.GetStatus.class);
        if (statusEvent != null) {
            notifyStatus(session.isStarted());
        }

    }

    @Subscribe
    public void onEvent(CommandEvents.Annotate annotate) {
        final String desc = annotate.annotation;
        if (desc.length() == 0) {
            session.clearDescription();
        } else {
            session.setDescription(desc);
            EventBus.getDefault().post(new ServiceEvents.AnnotationStatus(false));

            if (session.isStarted()) {
                startGpsManager();
            } else {
                logOnce();
            }
        }
        EventBus.getDefault().removeStickyEvent(CommandEvents.Annotate.class);
    }

    @Subscribe
    public void onEvent(CommandEvents.LogOnce logOnce) {
        logOnce();
    }

    @Subscribe
    public void onEvent(ServiceEvents.ActivityRecognitionEvent activityRecognitionEvent) {

        session.setLatestDetectedActivity(activityRecognitionEvent.result.getMostProbableActivity());

        if (!preferenceHelper.shouldNotLogIfUserIsStill()) {
            session.setUserStillSinceTimeStamp(0);
            return;
        }

        if (activityRecognitionEvent.result.getMostProbableActivity().getType() == DetectedActivity.STILL) {
            if (session.getUserStillSinceTimeStamp() == 0) {
                startGpsManager();
                session.setUserStillSinceTimeStamp(System.currentTimeMillis());
            }

        } else {
            //Reset the still-since timestamp
            session.setUserStillSinceTimeStamp(0);
            startGpsManager();
        }
    }

    /**
     * Can be used from calling classes as the go-between for methods and
     * properties.
     */
    class GpsLoggingBinder extends Binder {
        public GpsLoggingService getService() {
            return GpsLoggingService.this;
        }
    }
}
