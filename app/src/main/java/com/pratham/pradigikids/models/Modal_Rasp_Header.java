package com.pratham.pradigikids.models;

import com.google.gson.annotations.SerializedName;
import com.pratham.pradigikids.util.PD_Constant;

public class Modal_Rasp_Header {

    @SerializedName("author")
    private String author;
    @SerializedName("description")
    private String description;
    @SerializedName("id")
    private String id;
    @SerializedName("last_updated")
    private String last_updated;
    @SerializedName("lang_code")
    private String lang_code;
    @SerializedName("lang_name")
    private String lang_name;
    @SerializedName("name")
    private String name;
    @SerializedName("root")
    private String root;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("version")
    private int version;
    @SerializedName("available")
    private boolean available;
    @SerializedName("num_coach_contents")
    private int num_coach_contents;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    private String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

    private String getLang_name() {
        return lang_name;
    }

    public void setLang_name(String lang_name) {
        this.lang_name = lang_name;
    }

    private String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    private int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getNum_coach_contents() {
        return num_coach_contents;
    }

    public void setNum_coach_contents(int num_coach_contents) {
        this.num_coach_contents = num_coach_contents;
    }

    public Modal_ContentDetail setContentToConfigNodeStructure(Modal_Rasp_Header modal_rasp_header) {
        Modal_ContentDetail modal_contentDetail = null;
        try {
            modal_contentDetail = new Modal_ContentDetail();
            modal_contentDetail.setNodeid(modal_rasp_header.getId());
            modal_contentDetail.setNodetype("topic");
            modal_contentDetail.setNodetitle(modal_rasp_header.getName());
            modal_contentDetail.setNodekeywords("");
            modal_contentDetail.setNodeeage("");
            modal_contentDetail.setNodedesc(modal_rasp_header.getDescription());
            modal_contentDetail.setNodeimage("");
            modal_contentDetail.setNodeserverimage("");
            modal_contentDetail.setResourceid(modal_rasp_header.getRoot());
            modal_contentDetail.setResourcetype("topic");
            modal_contentDetail.setContentType("folder");
            modal_contentDetail.setResourcepath("");
            modal_contentDetail.setLevel(modal_rasp_header.getVersion());
            modal_contentDetail.setContent_language(modal_rasp_header.getLang_name());
            modal_contentDetail.setParentid("0");
            modal_contentDetail.setContentType(PD_Constant.FOLDER);
            modal_contentDetail.setDownloaded(false);
            modal_contentDetail.setOnSDCard(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modal_contentDetail;
    }
}