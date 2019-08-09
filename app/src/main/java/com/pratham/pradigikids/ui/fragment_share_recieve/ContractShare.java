package com.pratham.pradigikids.ui.fragment_share_recieve;

import android.support.v4.app.Fragment;

import com.pratham.pradigikids.models.File_Model;
import com.pratham.pradigikids.models.Modal_ContentDetail;
import com.pratham.pradigikids.models.Modal_ReceivingFilesThroughFTP;

import java.io.File;
import java.util.ArrayList;

public interface ContractShare {
    interface shareView {
        void hotspotStarted();

        void onWifiConnected(String ssid);

        void fileItemClicked(Modal_ContentDetail detail, int position);

        void sendItemChecked(File_Model detail, int position);

        void showFilesList(ArrayList<File_Model> contents, String parentId);

        void disconnectFTP();

        void closeFTPJoin();

        void showRecieving(ArrayList<Modal_ReceivingFilesThroughFTP> filesRecieving);

        void ftpConnected_showFolders();

        void ftpConnectionFailed();

        void animateHamburger();

    }

    interface sharePresenter {
        void connectFTP(String ip);

        void showFolders(Modal_ContentDetail detail);

        void traverseFolderBackward();

        void connectToWify(String ssid, String wifipass);

        void sendFiles(Modal_ContentDetail detail);

        void readRecievedFiles(File filePath);

        void showFilesRecieving(File filePath);

        void setView(Fragment fragmentShareRecieve);

        void startTimer();

        void sendProfiles();

        void sendUsages();

        void connectToAddedSSID(String ssid);

        void viewDestroyed();

        void connectionFailed();
    }
}
