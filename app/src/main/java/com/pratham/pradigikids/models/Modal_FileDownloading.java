package com.pratham.pradigikids.models;

import android.support.annotation.NonNull;

public class Modal_FileDownloading implements Comparable {
    private String downloadId;
    private String filename;
    private int progress;
    private Modal_ContentDetail contentDetail;
    private String remaining_time;

    public Modal_ContentDetail getContentDetail() {
        return contentDetail;
    }

    public void setContentDetail(Modal_ContentDetail contentDetail) {
        this.contentDetail = contentDetail;
    }

    public String getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(String downloadId) {
        this.downloadId = downloadId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getRemaining_time() {
        return remaining_time;
    }

    public void setRemaining_time(String remaining_time) {
        this.remaining_time = remaining_time;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Modal_FileDownloading compare = (Modal_FileDownloading) o;
        if (compare.getContentDetail().getNodeid() != null) {
            if (compare.getDownloadId().equals(this.downloadId) && compare.getProgress() == this.progress)
                return 0;
            else return 1;
        } else
            return 0;
    }
}
