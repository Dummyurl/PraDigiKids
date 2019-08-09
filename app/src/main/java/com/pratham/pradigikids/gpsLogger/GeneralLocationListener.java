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
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import com.pratham.pradigikids.util.PD_Constant;

import java.util.Iterator;

class GeneralLocationListener implements LocationListener, GpsStatus.Listener, GpsStatus.NmeaListener {

    private static final String TAG = GeneralLocationListener.class.getSimpleName();
    private static GpsLoggingService loggingService;
    private final String listenerName;
    private final Session session = Session.getInstance();
    private String latestHdop;
    private String latestPdop;
    private String latestVdop;
    private String geoIdHeight;
    private String ageOfDgpsData;
    private String dgpsId;
    private int satellitesUsedInFix;

    GeneralLocationListener(GpsLoggingService activity, String name) {
        loggingService = activity;
        listenerName = name;
    }

    /**
     * Event raised when a new fix is received.
     */
    public void onLocationChanged(Location loc) {

        try {
            if (loc != null) {
                Bundle b = new Bundle();
                b.putString(PD_Constant.GPS_HDOP, this.latestHdop);
                b.putString(PD_Constant.GPS_PDOP, this.latestPdop);
                b.putString(PD_Constant.GPS_VDOP, this.latestVdop);
                b.putString(PD_Constant.GPS_GEOIDHEIGHT, this.geoIdHeight);
                b.putString(PD_Constant.GPS_AGEOFDGPSDATA, this.ageOfDgpsData);
                b.putString(PD_Constant.GPS_DGPSID, this.dgpsId);

                b.putBoolean(PD_Constant.GPS_PASSIVE, listenerName.equalsIgnoreCase(PD_Constant.GPS_PASSIVE));
                b.putString(PD_Constant.GPS_LISTENER, listenerName);
                b.putInt(PD_Constant.GPS_SATELLITES_FIX, satellitesUsedInFix);
                b.putString(PD_Constant.GPS_DETECTED_ACTIVITY, session.getLatestDetectedActivityName());

                loc.setExtras(b);
                loggingService.onLocationChanged(loc);

                this.latestHdop = "";
                this.latestPdop = "";
                this.latestVdop = "";
                session.setLatestDetectedActivity(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onProviderDisabled(String provider) {
        loggingService.restartGpsManagers();
    }

    public void onProviderEnabled(String provider) {
        loggingService.restartGpsManagers();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (status == LocationProvider.OUT_OF_SERVICE) {
            loggingService.stopManagerAndResetAlarm();
        }

        if (status == LocationProvider.AVAILABLE) {
            Log.d(TAG, "onStatusChanged:AVAILABLE ");
        }

        if (status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
            Log.d(TAG, "onStatusChanged: TEMPORARILY_UNAVAILABLE");
        }
    }

    public void onGpsStatusChanged(int event) {
        switch (event) {
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                Log.d(TAG, "onGpsStatusChanged: GPS_EVENT_FIRST_FIX");
                break;

            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:

                @SuppressLint("MissingPermission") GpsStatus status = loggingService.gpsLocationManager.getGpsStatus(null);

                int maxSatellites = status.getMaxSatellites();

                Iterator<GpsSatellite> it = status.getSatellites().iterator();
                int satellitesVisible = 0;
                satellitesUsedInFix = 0;

                while (it.hasNext() && satellitesVisible <= maxSatellites) {
                    GpsSatellite sat = it.next();
                    if (sat.usedInFix()) {
                        satellitesUsedInFix++;
                    }
                    satellitesVisible++;
                }
                Log.d(TAG, "onGpsStatusChanged: " + satellitesVisible + " satellites");
                loggingService.setSatelliteInfo(satellitesVisible);
                break;

            case GpsStatus.GPS_EVENT_STARTED:
                Log.d(TAG, "onGpsStatusChanged: GPS_EVENT_STARTED");
                break;

            case GpsStatus.GPS_EVENT_STOPPED:
                Log.d(TAG, "onGpsStatusChanged: GPS_EVENT_STOPPED");
                break;
        }
    }

    @Override
    public void onNmeaReceived(long timestamp, String nmeaSentence) {
//        loggingService.onNmeaSentence(timestamp, nmeaSentence);

        if (Strings.isNullOrEmpty(nmeaSentence)) {
            return;
        }
        NmeaSentence nmea = new NmeaSentence(nmeaSentence);
        if (nmea.isLocationSentence()) {
            if (nmea.getLatestPdop() != null) {
                this.latestPdop = nmea.getLatestPdop();
            }

            if (nmea.getLatestHdop() != null) {
                this.latestHdop = nmea.getLatestHdop();
            }

            if (nmea.getLatestVdop() != null) {
                this.latestVdop = nmea.getLatestVdop();
            }

            if (nmea.getGeoIdHeight() != null) {
                this.geoIdHeight = nmea.getGeoIdHeight();
            }

            if (nmea.getAgeOfDgpsData() != null) {
                this.ageOfDgpsData = nmea.getAgeOfDgpsData();
            }

            if (nmea.getDgpsId() != null) {
                this.dgpsId = nmea.getDgpsId();
            }

        }

    }
}
