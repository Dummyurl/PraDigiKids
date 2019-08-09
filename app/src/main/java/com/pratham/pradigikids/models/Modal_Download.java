package com.pratham.pradigikids.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.pratham.pradigikids.ui.fragment_content.ContentContract;

import java.util.ArrayList;

public class Modal_Download implements Parcelable {
    public static final Creator<Modal_Download> CREATOR = new Creator<Modal_Download>() {
        @Override
        public Modal_Download createFromParcel(Parcel in) {
            return new Modal_Download(in);
        }

        @Override
        public Modal_Download[] newArray(int size) {
            return new Modal_Download[size];
        }
    };
    private String url;
    private String dir_path;
    private String f_name;
    private String folder_name;
    private Modal_ContentDetail content;
    private ContentContract.contentPresenter contentPresenter;
    private ArrayList<Modal_ContentDetail> levelContents;

    public Modal_Download(Parcel in) {
        url = in.readString();
        dir_path = in.readString();
        f_name = in.readString();
        folder_name = in.readString();
        content = in.readParcelable(Modal_ContentDetail.class.getClassLoader());
        levelContents = in.createTypedArrayList(Modal_ContentDetail.CREATOR);
    }

    public Modal_Download() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDir_path() {
        return dir_path;
    }

    public void setDir_path(String dir_path) {
        this.dir_path = dir_path;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public Modal_ContentDetail getContent() {
        return content;
    }

    public void setContent(Modal_ContentDetail content) {
        this.content = content;
    }

    public ContentContract.contentPresenter getContentPresenter() {
        return contentPresenter;
    }

    public void setContentPresenter(ContentContract.contentPresenter contentPresenter) {
        this.contentPresenter = contentPresenter;
    }

    public ArrayList<Modal_ContentDetail> getLevelContents() {
        return levelContents;
    }

    public void setLevelContents(ArrayList<Modal_ContentDetail> levelContents) {
        this.levelContents = levelContents;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(dir_path);
        dest.writeString(f_name);
        dest.writeString(folder_name);
        dest.writeParcelable(content, flags);
        dest.writeTypedList(levelContents);
    }
}
