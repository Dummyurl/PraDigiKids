package com.pratham.pradigikids.ui.fragment_content;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.liulishuo.okdownload.DownloadTask;
import com.pratham.pradigikids.models.EventMessage;
import com.pratham.pradigikids.models.Modal_ContentDetail;
import com.pratham.pradigikids.models.Modal_FileDownloading;

import java.util.ArrayList;
import java.util.List;

public interface ContentContract {
    interface contentView {
        void showNoConnectivity();

        void displayContents(List<Modal_ContentDetail> content);

        void displayHeader(Modal_ContentDetail contentDetail);

        void hideViews();

        void exitApp();

        void increaseNotification(int number);

        void decreaseNotification(int number, Modal_ContentDetail detail, ArrayList<String> selectedNodeIds);

        void displayLevel(ArrayList<Modal_ContentDetail> levelContents);

        void onDownloadError(EventMessage message);

        void dismissDialog();

        void animateHamburger();

        void displayDLContents(List<Modal_ContentDetail> details);
    }

    interface contentPresenter {
        void setView(FragmentContent context);

        void downloadContent(Modal_ContentDetail contentDetail);

        void ondownloadError(String downloadId);

        void checkConnectionForRaspberry();

        void showPreviousContent();

        void getContent(Modal_ContentDetail contentDetail);

        void eventFileDownloadStarted(EventMessage message);

        void eventUpdateFileProgress(EventMessage message);

        void eventOnDownloadCompleted(EventMessage message);

        void eventOnDownloadFailed(EventMessage message);

        void fileDownloadStarted(String downloadID, Modal_FileDownloading modal_fileDownloading);

        void updateFileProgress(String downloadID, Modal_FileDownloading mfd);

        void onDownloadCompleted(String downloadID, Modal_ContentDetail content);

        void broadcast_downloadings();

        void parseSD_UriandPath(Intent data);

        void getContent();

        void viewDestroyed();

        void deleteContent(Modal_ContentDetail contentItem);

        void currentDownloadRunning(String nodeid, DownloadTask task);

        void cancelDownload(String downloadId);

        void openDeepLinkContent(String string);

        List<Modal_ContentDetail> getContentList();

        boolean isFilesDownloading();

        void downloadCompleteFolder(Modal_ContentDetail contentItem);
    }

    interface contentClick {
        void onfolderClicked(int position, Modal_ContentDetail contentDetail);

        void onDownloadClicked(int position, Modal_ContentDetail contentDetail, View reveal_view, View start_reveal_item);

        void openContent(int position, Modal_ContentDetail contentDetail);

        void deleteContent(int pos, Modal_ContentDetail contentItem);

        void completeFolderDownload(int pos, Modal_ContentDetail contentItem);
    }

    interface mainView {
//        void showNotificationBadge(int downloadNumber);
//
//        void hideNotificationBadge(int number);
    }
}
