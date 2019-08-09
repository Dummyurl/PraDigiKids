package com.pratham.pradigikids.models;

import com.google.gson.annotations.SerializedName;
import com.pratham.pradigikids.util.PD_Constant;

import java.net.URLDecoder;
import java.util.List;

public class Modal_Rasp_Content {

    @SerializedName("coach_content")
    private boolean coachContent;

    @SerializedName("num_coach_contents")
    private int numCoachContents;

    @SerializedName("parent")
    private String parent;

    @SerializedName("content_id")
    private String contentId;

    @SerializedName("author")
    private String author;

    @SerializedName("kind")
    private String kind;

    @SerializedName("available")
    private boolean available;

    @SerializedName("description")
    private String description;

    @SerializedName("title")
    private String title;

    @SerializedName("license_description")
    private Object licenseDescription;

    @SerializedName("license_name")
    private Object licenseName;

    @SerializedName("progress_fraction")
    private Object progressFraction;

    @SerializedName("assessmentmetadata")
    private List<Object> assessmentmetadata;

    @SerializedName("license_owner")
    private String licenseOwner;

    @SerializedName("files")
    private List<Modal_Rasp_ContentFilesItem> files;

    @SerializedName("id")
    private String id;

    @SerializedName("pk")
    private String pk;

    @SerializedName("lang")
    private Modal_Rasp_ContentLang lang;

    @SerializedName("channel_id")
    private String channelId;

    @SerializedName("sort_order")
    private int sortOrder;

    public void setCoachContent(boolean coachContent) {
        this.coachContent = coachContent;
    }

    public boolean isCoachContent() {
        return coachContent;
    }

    public void setNumCoachContents(int numCoachContents) {
        this.numCoachContents = numCoachContents;
    }

    public int getNumCoachContents() {
        return numCoachContents;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    private String getParent() {
        return parent;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    private String getContentId() {
        return contentId;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    private String getKind() {
        return kind;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String getTitle() {
        return title;
    }

    public void setLicenseDescription(Object licenseDescription) {
        this.licenseDescription = licenseDescription;
    }

    public Object getLicenseDescription() {
        return licenseDescription;
    }

    public void setLicenseName(Object licenseName) {
        this.licenseName = licenseName;
    }

    public Object getLicenseName() {
        return licenseName;
    }

    public void setProgressFraction(Object progressFraction) {
        this.progressFraction = progressFraction;
    }

    public Object getProgressFraction() {
        return progressFraction;
    }

    public void setAssessmentmetadata(List<Object> assessmentmetadata) {
        this.assessmentmetadata = assessmentmetadata;
    }

    public List<Object> getAssessmentmetadata() {
        return assessmentmetadata;
    }

    public void setLicenseOwner(String licenseOwner) {
        this.licenseOwner = licenseOwner;
    }

    public String getLicenseOwner() {
        return licenseOwner;
    }

    public void setFiles(List<Modal_Rasp_ContentFilesItem> files) {
        this.files = files;
    }

    private List<Modal_Rasp_ContentFilesItem> getFiles() {
        return files;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String getId() {
        return id;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getPk() {
        return pk;
    }

    public void setLang(Modal_Rasp_ContentLang lang) {
        this.lang = lang;
    }

    public Modal_Rasp_ContentLang getLang() {
        return lang;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public Modal_ContentDetail setContentToConfigNodeStructure(Modal_Rasp_Content modal_rasp_content) {
        Modal_ContentDetail modal_contentDetail = null;
        try {
            modal_contentDetail = new Modal_ContentDetail();
            modal_contentDetail.setNodeid(modal_rasp_content.getId());
            modal_contentDetail.setAltnodeid(modal_rasp_content.getId());
            switch (modal_rasp_content.getKind()) {
                case "html5":
                    modal_contentDetail.setNodetype("Resource");
                    modal_contentDetail.setResourcetype("Game");
                    modal_contentDetail.setContentType("file");
                    break;
                case "video":
                    modal_contentDetail.setNodetype("Resource");
                    modal_contentDetail.setResourcetype("Video");
                    modal_contentDetail.setContentType("file");
                    break;
                case "document":
                    modal_contentDetail.setNodetype("Resource");
                    modal_contentDetail.setResourcetype("Pdf");
                    modal_contentDetail.setContentType("file");
                    break;
                default:
                    modal_contentDetail.setNodetype(modal_rasp_content.getKind());
                    modal_contentDetail.setResourcetype(modal_rasp_content.getKind());
                    modal_contentDetail.setContentType("folder");
            }
            modal_contentDetail.setNodetitle(modal_rasp_content.getTitle());
            modal_contentDetail.setNodeeage("");
            modal_contentDetail.setNodedesc(modal_rasp_content.getDescription());
            for (Modal_Rasp_ContentFilesItem filesItem : modal_rasp_content.getFiles()) {
                if (filesItem.isThumbnail()) {
                    String filename = URLDecoder.decode(filesItem.getDownloadUrl(), "UTF-8")
                            .substring(URLDecoder.decode(filesItem.getDownloadUrl(), "UTF-8").lastIndexOf('/') + 1);
                    modal_contentDetail.setNodeimage(filename);
                    modal_contentDetail.setNodeserverimage(PD_Constant.RASP_IP + URLDecoder.decode(filesItem.getDownloadUrl(), "UTF-8"));
                    modal_contentDetail.setKolibriNodeImageUrl(PD_Constant.RASP_IP + filesItem.getStorageUrl());
                } else {
                    String filename = URLDecoder.decode(filesItem.getDownloadUrl(), "UTF-8")
                            .substring(URLDecoder.decode(filesItem.getDownloadUrl(), "UTF-8").lastIndexOf('/') + 1);
                    //fileNameWithoutExtn
                    filename = filename.substring(0, filename.lastIndexOf("."));
                    if (modal_contentDetail.getResourcetype().equalsIgnoreCase("Game")) {
                        modal_contentDetail.setNodekeywords(PD_Constant.RASP_IP + /*URLDecoder.decode(*/filesItem.getDownloadUrl()/*, "UTF-8")*/);
                        modal_contentDetail.setResourcepath(filename + "/index.html");
                    } else if (modal_contentDetail.getResourcetype().equalsIgnoreCase("Video")) {
                        modal_contentDetail.setNodekeywords(PD_Constant.RASP_IP + filesItem.getStorageUrl());
                        filename = filesItem.getStorageUrl()
                                .substring(filesItem.getStorageUrl().lastIndexOf('/') + 1);
                        filename = filename.substring(0, filename.lastIndexOf("."));
                        modal_contentDetail.setResourcepath(filename + ".mp4");
                    } else if (modal_contentDetail.getResourcetype().equalsIgnoreCase("Pdf")) {
                        modal_contentDetail.setNodekeywords(PD_Constant.RASP_IP + filesItem.getStorageUrl());
                        filename = filesItem.getStorageUrl()
                                .substring(filesItem.getStorageUrl().lastIndexOf('/') + 1);
                        filename = filename.substring(0, filename.lastIndexOf("."));
                        modal_contentDetail.setResourcepath(filename + ".pdf");
                    }
                    modal_contentDetail.setLevel(filesItem.getFileSize());
                }
            }
            modal_contentDetail.setResourceid(modal_rasp_content.getContentId());
            modal_contentDetail.setParentid(modal_rasp_content.getParent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modal_contentDetail;
    }
}


