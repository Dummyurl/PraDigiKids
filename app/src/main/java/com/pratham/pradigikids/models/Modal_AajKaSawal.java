package com.pratham.pradigikids.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Modal_AajKaSawal implements Parcelable {
    @SerializedName("aksVersion")
    private String aksVersion;
    @SerializedName("nodeId")
    private String nodeId;
    @SerializedName("nodeType")
    private String nodeType;
    @SerializedName("nodeTitle")
    private String nodeTitle;
    @SerializedName("QueId")
    private String QueId;
    @SerializedName("Question")
    private String Question;
    @SerializedName("QuestionType")
    private String QuestionType;
    @SerializedName("Option1")
    private String Option1;
    @SerializedName("Option2")
    private String Option2;
    @SerializedName("Option3")
    private String Option3;
    @SerializedName("Option4")
    private String Option4;
    @SerializedName("Answer")
    private String Answer;
    @SerializedName("resourceName")
    private String resourceName;
    @SerializedName("resourceType")
    private String resourceType;
    @SerializedName("resourceId")
    private String resourceId;
    @SerializedName("resourcePath")
    private String resourcePath;
    @SerializedName("programLanguage")
    private String programLanguage;
    @SerializedName("nodelist")
    private List<Modal_AajKaSawal> nodelist;

    public static final Creator<Modal_AajKaSawal> CREATOR = new Creator<Modal_AajKaSawal>() {
        @Override
        public Modal_AajKaSawal createFromParcel(Parcel in) {
            return new Modal_AajKaSawal(in);
        }

        @Override
        public Modal_AajKaSawal[] newArray(int size) {
            return new Modal_AajKaSawal[size];
        }
    };

    private Modal_AajKaSawal(Parcel in) {
        aksVersion = in.readString();
        nodeId = in.readString();
        nodeType = in.readString();
        nodeTitle = in.readString();
        QueId = in.readString();
        Question = in.readString();
        QuestionType = in.readString();
        Option1 = in.readString();
        Option2 = in.readString();
        Option3 = in.readString();
        Option4 = in.readString();
        Answer = in.readString();
        resourceName = in.readString();
        resourceType = in.readString();
        resourceId = in.readString();
        resourcePath = in.readString();
        programLanguage = in.readString();
        nodelist = in.createTypedArrayList(Modal_AajKaSawal.CREATOR);
    }

    public String getAksVersion() {
        return aksVersion;
    }

    public void setAksVersion(String aksVersion) {
        this.aksVersion = aksVersion;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeTitle() {
        return nodeTitle;
    }

    public void setNodeTitle(String nodeTitle) {
        this.nodeTitle = nodeTitle;
    }

    public String getQueId() {
        return QueId;
    }

    public void setQueId(String queId) {
        QueId = queId;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getQuestionType() {
        return QuestionType;
    }

    public void setQuestionType(String questionType) {
        QuestionType = questionType;
    }

    public String getOption1() {
        return Option1;
    }

    public void setOption1(String option1) {
        Option1 = option1;
    }

    public String getOption2() {
        return Option2;
    }

    public void setOption2(String option2) {
        Option2 = option2;
    }

    public String getOption3() {
        return Option3;
    }

    public void setOption3(String option3) {
        Option3 = option3;
    }

    public String getOption4() {
        return Option4;
    }

    public void setOption4(String option4) {
        Option4 = option4;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getProgramLanguage() {
        return programLanguage;
    }

    public void setProgramLanguage(String programLanguage) {
        this.programLanguage = programLanguage;
    }

    public List<Modal_AajKaSawal> getNodelist() {
        return nodelist;
    }

    public void setNodelist(List<Modal_AajKaSawal> nodelist) {
        this.nodelist = nodelist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(aksVersion);
        dest.writeString(nodeId);
        dest.writeString(nodeType);
        dest.writeString(nodeTitle);
        dest.writeString(QueId);
        dest.writeString(Question);
        dest.writeString(QuestionType);
        dest.writeString(Option1);
        dest.writeString(Option2);
        dest.writeString(Option3);
        dest.writeString(Option4);
        dest.writeString(Answer);
        dest.writeString(resourceName);
        dest.writeString(resourceType);
        dest.writeString(resourceId);
        dest.writeString(resourcePath);
        dest.writeString(programLanguage);
        dest.writeTypedList(nodelist);
    }
}
