package com.pratham.pradigikids.models;

import com.google.gson.annotations.SerializedName;

public class Modal_Rasp_ContentFilesItem {

    @SerializedName("extension")
    private String extension;

    @SerializedName("thumbnail")
    private boolean thumbnail;

    @SerializedName("available")
    private boolean available;

    @SerializedName("download_url")
    private String downloadUrl;

    @SerializedName("supplementary")
    private boolean supplementary;

    @SerializedName("storage_url")
    private String storageUrl;

    @SerializedName("id")
    private String id;

    @SerializedName("preset")
    private String preset;

    @SerializedName("priority")
    private int priority;

    @SerializedName("lang")
    private Object lang;

    @SerializedName("file_size")
    private int fileSize;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public boolean isThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(boolean thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public boolean isSupplementary() {
        return supplementary;
    }

    public void setSupplementary(boolean supplementary) {
        this.supplementary = supplementary;
    }

    public String getStorageUrl() {
        return storageUrl;
    }

    public void setStorageUrl(String storageUrl) {
        this.storageUrl = storageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPreset() {
        return preset;
    }

    public void setPreset(String preset) {
        this.preset = preset;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Object getLang() {
        return lang;
    }

    public void setLang(Object lang) {
        this.lang = lang;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
}