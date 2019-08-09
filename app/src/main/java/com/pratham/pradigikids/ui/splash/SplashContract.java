package com.pratham.pradigikids.ui.splash;

import android.content.Intent;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;

public interface SplashContract {
    interface splashview {
        void showAppUpdateDialog();

        void redirectToDashboard();

        void redirectToAvatar();

        void redirectToAttendance();

        void signInUsingGoogle();

        void googleSignInFailed();

        void loadSplash();

        void showEnterPrathamCodeDialog();
    }

    interface splashPresenter {
        void checkIfContentinSDCard();

        void populateDefaultDB();

        void checkConnectivity();

        void checkStudentList();

        void validateSignIn(Intent data);

        void checkVersion(String latestVersion);

        void clearPreviousBuildData();

        GoogleApiClient configureSignIn();

        void onLocationChanged(Location location);

        void startGpsTimer();

        void savePrathamCode(String code);

        void checkPrathamCode();
    }
}
