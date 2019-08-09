package com.pratham.pradigikids.services;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.pratham.pradigikids.PrathamApplication;
import com.pratham.pradigikids.gpsLogger.GpsLoggingService;
import com.pratham.pradigikids.gpsLogger.Session;
import com.pratham.pradigikids.models.Modal_Status;
import com.pratham.pradigikids.util.PD_Constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

import static com.pratham.pradigikids.PrathamApplication.statusDao;

public class LocationService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = LocationService.class.getSimpleName();
    private final Context context;
    private GoogleApiClient mGoogleApiClient;

    public LocationService(Context context) {
        this.context = context;
    }

    private void startLocationListener() {
        long mLocTrackingInterval = 1000 * 15; // 15 sec
        float trackingDistance = 0;
        LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;
        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(trackingAccuracy)
                .setDistance(trackingDistance)
                .setInterval(mLocTrackingInterval);
        SmartLocation.with(context)
                .location()
                .continuous()
                .config(builder.build())
                .start(location -> {
                    Modal_Status statusObj = new Modal_Status();

                    statusObj.statusKey = "Latitude";
                    statusObj.value = String.valueOf(location.getLatitude());
                    statusDao.insert(statusObj);

                    statusObj.statusKey = "Longitude";
                    statusObj.value = String.valueOf(location.getLongitude());
                    statusDao.insert(statusObj);

                    statusObj.statusKey = "GPSDateTime";
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
                    Date gdate = new Date(location.getTime());
                    statusObj.value = format.format(gdate);
                    statusDao.insert(statusObj);

                    Log.d(TAG, "onLocationUpdated:" + location.getLatitude() + ":::" + location.getLongitude());
                });
    }

    /**
     * Provides a connection to the GPS Logging Service
     */
    private final ServiceConnection gpsServiceConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: \"Disconnected from GPSLoggingService from MainActivity\"");
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: Connected to GPSLoggingService from MainActivity");
        }
    };

    public void checkLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startLocationListenerServiceAccordingToDevice();
        } else {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API).addConnectionCallbacks(LocationService.this)
                    .addOnConnectionFailedListener(LocationService.this)
                    .build();
            mGoogleApiClient.connect();
            showSettingDialog();
        }
    }

    private void startLocationListenerServiceAccordingToDevice() {
        if (PrathamApplication.useSatelliteGPS) {
            Intent serviceIntent = new Intent(context, GpsLoggingService.class);
            serviceIntent.putExtra(PD_Constant.GPS_LOGGER_IMMEDIATE_START, true);
            // Start the service in case it isn't already running
            ContextCompat.startForegroundService(PrathamApplication.getInstance().getApplicationContext(), serviceIntent);
            // Now bind to service
            context.bindService(serviceIntent, gpsServiceConnection, Context.BIND_AUTO_CREATE);
            Session.getInstance().setBoundToService(true);
        } else
            startLocationListener();
    }

    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            final LocationSettingsStates state = result1.getLocationSettingsStates();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    startLocationListenerServiceAccordingToDevice();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult((Activity) context, 10);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are not satisfied. However, we have no way to fix the
                    // settings so we won't show the dialog.
                    break;
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationListenerServiceAccordingToDevice();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
    }

    public boolean checkLocationEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
