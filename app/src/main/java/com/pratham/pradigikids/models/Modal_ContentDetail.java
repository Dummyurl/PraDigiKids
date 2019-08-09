package com.pratham.pradigikids.models;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "TableContent")
public class Modal_ContentDetail implements Comparable, Parcelable {
    @PrimaryKey
    @NonNull
    @SerializedName("nodeid")
    private String nodeid;
    @SerializedName("nodetype")
    private String nodetype;
    @SerializedName("nodetitle")
    private String nodetitle;
    @SerializedName("nodekeywords")
    private String nodekeywords;
    @SerializedName("nodeeage")
    private String nodeeage;
    @SerializedName("nodedesc")
    private String nodedesc;
    @SerializedName("nodeimage")
    private String nodeimage;
    @SerializedName("nodeserverimage")
    private String nodeserverimage;
    @SerializedName("resourceid")
    private String resourceid;
    @SerializedName("resourcetype")
    private String resourcetype;
    @SerializedName("resourcepath")
    private String resourcepath;
    @SerializedName("level")
    private int level;
    @SerializedName("content_language")
    private String content_language;
    @SerializedName("parentid")
    private String parentid;
    private String contentType;
    private boolean isDownloaded = false;
    //for offline content sharing
    private boolean onSDCard = false;
    @SerializedName("altnodeid")
    private String altnodeid;
    @SerializedName("version")
    private String version;
    @SerializedName("ischildresource")
    private boolean ischildresource;
    @Ignore
    private String kolibriNodeImageUrl;


    public Modal_ContentDetail() {
    }

    protected Modal_ContentDetail(Parcel in) {
        nodeid = in.readString();
        nodetype = in.readString();
        nodetitle = in.readString();
        nodekeywords = in.readString();
        nodeeage = in.readString();
        nodedesc = in.readString();
        nodeimage = in.readString();
        nodeserverimage = in.readString();
        resourceid = in.readString();
        resourcetype = in.readString();
        resourcepath = in.readString();
        level = in.readInt();
        content_language = in.readString();
        parentid = in.readString();
        contentType = in.readString();
        isDownloaded = in.readByte() != 0;
        onSDCard = in.readByte() != 0;
        kolibriNodeImageUrl = in.readString();
        altnodeid = in.readString();
        version = in.readString();
        ischildresource = in.readByte() != 0;
    }

    public static final Creator<Modal_ContentDetail> CREATOR = new Creator<Modal_ContentDetail>() {
        @Override
        public Modal_ContentDetail createFromParcel(Parcel in) {
            return new Modal_ContentDetail(in);
        }

        @Override
        public Modal_ContentDetail[] newArray(int size) {
            return new Modal_ContentDetail[size];
        }
    };

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public String getNodetype() {
        return nodetype;
    }

    public void setNodetype(String nodetype) {
        this.nodetype = nodetype;
    }

    public String getNodetitle() {
        return nodetitle;
    }

    public void setNodetitle(String nodetitle) {
        this.nodetitle = nodetitle;
    }

    public String getNodekeywords() {
        return nodekeywords;
    }

    public void setNodekeywords(String nodekeywords) {
        this.nodekeywords = nodekeywords;
    }

    public String getNodeeage() {
        return nodeeage;
    }

    public void setNodeeage(String nodeeage) {
        this.nodeeage = nodeeage;
    }

    public String getNodedesc() {
        return nodedesc;
    }

    public void setNodedesc(String nodedesc) {
        this.nodedesc = nodedesc;
    }

    public String getNodeimage() {
        return nodeimage;
    }

    public void setNodeimage(String nodeimage) {
        this.nodeimage = nodeimage;
    }

    public String getNodeserverimage() {
        return nodeserverimage;
    }

    public void setNodeserverimage(String nodeserverimage) {
        this.nodeserverimage = nodeserverimage;
    }

    public String getResourceid() {
        return resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    public String getResourcetype() {
        return resourcetype;
    }

    public void setResourcetype(String resourcetype) {
        this.resourcetype = resourcetype;
    }

    public String getResourcepath() {
        return resourcepath;
    }

    public void setResourcepath(String resourcepath) {
        this.resourcepath = resourcepath;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getContent_language() {
        return content_language;
    }

    public void setContent_language(String content_language) {
        this.content_language = content_language;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public boolean isOnSDCard() {
        return onSDCard;
    }

    public void setOnSDCard(boolean onSDCard) {
        this.onSDCard = onSDCard;
    }

    public String getKolibriNodeImageUrl() {
        return kolibriNodeImageUrl;
    }

    public void setKolibriNodeImageUrl(String kolibriNodeImageUrl) {
        this.kolibriNodeImageUrl = kolibriNodeImageUrl;
    }

    public String getAltnodeid() {
        return altnodeid;
    }

    public void setAltnodeid(String altnodeid) {
        this.altnodeid = altnodeid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isIschildresource() {
        return ischildresource;
    }

    public void setIschildresource(boolean ischildresource) {
        this.ischildresource = ischildresource;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Modal_ContentDetail compare = (Modal_ContentDetail) o;
        if (compare.getNodeid() != null) {
            if (compare.getNodeid().equalsIgnoreCase(this.nodeid) && compare.isDownloaded() == this.isDownloaded)
                return 0;
            else return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nodeid);
        dest.writeString(nodetype);
        dest.writeString(nodetitle);
        dest.writeString(nodekeywords);
        dest.writeString(nodeeage);
        dest.writeString(nodedesc);
        dest.writeString(nodeimage);
        dest.writeString(nodeserverimage);
        dest.writeString(resourceid);
        dest.writeString(resourcetype);
        dest.writeString(resourcepath);
        dest.writeInt(level);
        dest.writeString(content_language);
        dest.writeString(parentid);
        dest.writeString(contentType);
        dest.writeByte((byte) (isDownloaded ? 1 : 0));
        dest.writeByte((byte) (onSDCard ? 1 : 0));
        dest.writeString(kolibriNodeImageUrl);
        dest.writeString(altnodeid);
        dest.writeString(version);
        dest.writeByte((byte) (ischildresource ? 1 : 0));
    }
}
