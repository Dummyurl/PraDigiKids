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


import android.location.LocationManager;

import com.pratham.pradigikids.custom.shared_preference.FastSave;
import com.pratham.pradigikids.util.PD_Constant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PreferenceHelper {

    private static PreferenceHelper instance = null;

    /**
     * Use PreferenceHelper.getInstance()
     */
    private PreferenceHelper() {

    }

    public static PreferenceHelper getInstance() {
        if (instance == null) {
            instance = new PreferenceHelper();
//            instance.prefs = PreferenceManager.getDefaultSharedPreferences(AppSettings.getInstance().getApplicationContext());
        }

        return instance;
    }

    /**
     * The minimum seconds interval between logging points
     */
    public int getMinimumLoggingInterval() {
        return Strings.toInt(FastSave.getInstance().getString(PD_Constant.GPS_MINIMUM_INTERVAL, "600"), 600);
    }

    /**
     * Sets the minimum time interval between logging points
     *
     * @param minimumSeconds - in seconds
     */
    public void setMinimumLoggingInterval(int minimumSeconds) {
        FastSave.getInstance().saveString(PD_Constant.GPS_MINIMUM_INTERVAL, String.valueOf(minimumSeconds));
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString(PreferenceNames.MINIMUM_INTERVAL, String.valueOf(minimumSeconds));
//        editor.apply();
    }


    /**
     * The minimum distance, in meters, to have traveled before a point is recorded
     */
//    @ProfilePreference(name = PreferenceNames.MINIMUM_DISTANCE)
    public int getMinimumDistanceInterval() {
        return (Strings.toInt(FastSave.getInstance().getString(PD_Constant.GPS_MINIMUM_DISTANCE, "0"), 0));
    }

    /**
     * Sets the minimum distance to have traveled before a point is recorded
     *
     * @param distanceBeforeLogging - in meters
     */
    public void setMinimumDistanceInMeters(int distanceBeforeLogging) {
        FastSave.getInstance().saveString(PD_Constant.GPS_MINIMUM_DISTANCE, String.valueOf(distanceBeforeLogging));
//        prefs.edit().putString(PD_Constant.GPS_MINIMUM_DISTANCE, String.valueOf(distanceBeforeLogging)).apply();
    }


    /**
     * The minimum accuracy of a point before the point is recorded, in meters
     */
//    @ProfilePreference(name = PreferenceNames.MINIMUM_ACCURACY)
    public int getMinimumAccuracy() {
        return (Strings.toInt(FastSave.getInstance().getString(PD_Constant.GPS_MINIMUM_ACCURACY, "40"), 40));
    }

    public void setMinimumAccuracy(int minimumAccuracy) {
        FastSave.getInstance().saveString(PD_Constant.GPS_MINIMUM_ACCURACY, String.valueOf(minimumAccuracy));
//        prefs.edit().putString(PreferenceNames.MINIMUM_ACCURACY, String.valueOf(minimumAccuracy)).apply();
    }


    /**
     * Whether to keep GPS on between fixes
     */
//    @ProfilePreference(name = PreferenceNames.KEEP_GPS_ON_BETWEEN_FIXES)
    public boolean shouldKeepGPSOnBetweenFixes() {
        return FastSave.getInstance().getBoolean(PD_Constant.KEEP_GPS_ON_BETWEEN_FIXES, false);
    }

    /**
     * Set whether to keep GPS on between fixes
     */
    public void setShouldKeepGPSOnBetweenFixes(boolean keepFix) {
        FastSave.getInstance().saveBoolean(PD_Constant.KEEP_GPS_ON_BETWEEN_FIXES, keepFix);
//        prefs.edit().putBoolean(PreferenceNames.KEEP_GPS_ON_BETWEEN_FIXES, keepFix).apply();
    }


    /**
     * How long to keep retrying for a fix if one with the user-specified accuracy hasn't been found
     */
//    @ProfilePreference(name = PreferenceNames.LOGGING_RETRY_TIME)
    public int getLoggingRetryPeriod() {
        return (Strings.toInt(FastSave.getInstance().getString(PD_Constant.GPS_LOGGING_RETRY_TIME, "600"), 600));
    }


    /**
     * Sets how long to keep trying for an accurate fix
     *
     * @param retryInterval in seconds
     */
    public void setLoggingRetryPeriod(int retryInterval) {
        FastSave.getInstance().saveString(PD_Constant.GPS_LOGGING_RETRY_TIME, String.valueOf(retryInterval));
//        prefs.edit().putString(PreferenceNames.LOGGING_RETRY_TIME, String.valueOf(retryInterval)).apply();
    }

    /**
     * How long to keep retrying for an accurate point before giving up
     */
//    //@ProfilePreference(name = PreferenceNames.ABSOLUTE_TIMEOUT)
    public int getAbsoluteTimeoutForAcquiringPosition() {
        return (Strings.toInt(FastSave.getInstance().getString(PD_Constant.GPS_ABSOLUTE_TIMEOUT, "600"), 600));
    }

    /**
     * Sets how long to keep retrying for an accurate point before giving up
     *
     * @param absoluteTimeout in seconds
     */
    public void setAbsoluteTimeoutForAcquiringPosition(int absoluteTimeout) {
        FastSave.getInstance().saveString(PD_Constant.GPS_ABSOLUTE_TIMEOUT, String.valueOf(absoluteTimeout));
    }

    /**
     * Whether to hide the buttons when displaying the app notification
     */
    //@ProfilePreference(name = PD_Constant.HIDE_NOTIFICATION_BUTTONS)
    public boolean shouldHideNotificationButtons() {
        return FastSave.getInstance().getBoolean(PD_Constant.GPS_HIDE_NOTIFICATION_BUTTONS, false);
    }


    //@ProfilePreference(name = PD_Constant.HIDE_NOTIFICATION_FROM_STATUS_BAR)
    public boolean shouldHideNotificationFromStatusBar() {
        return FastSave.getInstance().getBoolean(PD_Constant.GPS_HIDE_NOTIFICATION_FROM_STATUS_BAR, false);
    }

    //@ProfilePreference(name = PD_Constant.DISPLAY_IMPERIAL)
//    public boolean shouldDisplayImperialUnits() {
//        return FastSave.getInstance().getBoolean(PD_Constant.DISPLAY_IMPERIAL, false);
//    }

    /**
     * Display format to use for lat long coordinates on screen
     * DEGREES_MINUTES_SECONDS, DEGREES_DECIMAL_MINUTES, DECIMAL_DEGREES
     */
    //@ProfilePreference(name = PD_Constant.LATLONG_DISPLAY_FORMAT)
//    public PD_Constant.DegreesDisplayFormat getDisplayLatLongFormat() {
//        String chosenValue = FastSave.getInstance().getString(PD_Constant.LATLONG_DISPLAY_FORMAT, "DEGREES_MINUTES_SECONDS");
//        return PD_Constant.DegreesDisplayFormat.valueOf(chosenValue);
//    }

//    public void setDisplayLatLongFormat(PD_Constant.DegreesDisplayFormat displayFormat) {
//        FastSave.getInstance().saveString(PD_Constant.GPS_LATLONG_DISPLAY_FORMAT, displayFormat.toString());
//    }

    /**
     * Gets a list of location providers that the app will listen to
     */
    //@ProfilePreference(name = PD_Constant.LOCATION_LISTENERS)
    public Set<String> getChosenListeners() {
        Set<String> defaultListeners = new HashSet<>(getDefaultListeners());
        return FastSave.getInstance().getSet(PD_Constant.GPS_LOCATION_LISTENERS, defaultListeners);
    }

    /**
     * Sets the list of location providers that the app will listen to given their array positions in {@link #getAvailableListeners()}.
     */
    public void setChosenListeners(Integer... listenerIndices) {
        Integer[] selectedItems = listenerIndices;
        final Set<String> chosenListeners = new HashSet<>();

        for (Integer selectedItem : selectedItems) {
            chosenListeners.add(getAvailableListeners().get(selectedItem));
        }

        if (chosenListeners.size() > 0) {
            setChosenListeners(chosenListeners);
        }
    }

    /**
     * Sets the list of location providers that the app will listen to
     *
     * @param chosenListeners a Set of listener names
     */
    private void setChosenListeners(Set<String> chosenListeners) {
        FastSave.getInstance().saveSet(PD_Constant.GPS_LOCATION_LISTENERS, chosenListeners);
    }

    /**
     * Default set of listeners
     */
    private List<String> getDefaultListeners() {
        List<String> listeners = new ArrayList<>();
        listeners.add(LocationManager.GPS_PROVIDER);
        listeners.add(LocationManager.NETWORK_PROVIDER);
        return listeners;
    }

    /**
     * All the possible listeners
     *
     * @return
     */
    private List<String> getAvailableListeners() {
        List<String> listeners = new ArrayList<>();
        listeners.add(LocationManager.GPS_PROVIDER);
        listeners.add(LocationManager.NETWORK_PROVIDER);
        return listeners;
    }

    //@ProfilePreference(name = PD_Constant.LOG_PASSIVE_LOCATIONS)
    public boolean shouldLogPassiveLocations() {
        return FastSave.getInstance().getBoolean(PD_Constant.GPS_LOG_PASSIVE_LOCATIONS, false);
    }

    /**
     * Whether to detect user activity and if the user is still, pause logging
     */
    //@ProfilePreference(name = PD_Constant.ACTIVITYRECOGNITION_DONTLOGIFSTILL)
    public boolean shouldNotLogIfUserIsStill() {
        return FastSave.getInstance().getBoolean(PD_Constant.GPS_ACTIVITYRECOGNITION_DONTLOGIFSTILL, false);
    }


    /**
     * Whether to subtract GeoID height from the reported altitude to get Mean Sea Level altitude instead of WGS84
     */
    //@ProfilePreference(name = PD_Constant.ALTITUDE_SHOULD_ADJUST)
    public boolean shouldAdjustAltitudeFromGeoIdHeight() {
        return FastSave.getInstance().getBoolean(PD_Constant.GPS_ALTITUDE_SHOULD_ADJUST, false);
    }

    /**
     * How much to subtract from the altitude reported
     */
    //@ProfilePreference(name = PD_Constant.ALTITUDE_SUBTRACT_OFFSET)
    public int getSubtractAltitudeOffset() {
        return Strings.toInt(FastSave.getInstance().getString(PD_Constant.GPS_ALTITUDE_SUBTRACT_OFFSET, "0"), 0);
    }
}
