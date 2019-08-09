package com.pratham.pradigikids.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Score")
public class Modal_Score {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("ScoreId")
    private int ScoreId;
    @SerializedName("SessionID")
    private String SessionID;
    @SerializedName("StudentID")
    private String StudentID;
    @SerializedName("GroupID")
    private String GroupID;
    @SerializedName("DeviceID")
    private String DeviceID;
    @SerializedName("ResourceID")
    private String ResourceID;
    @SerializedName("QuestionId")
    private int QuestionId;
    @SerializedName("ScoredMarks")
    private int ScoredMarks;
    @SerializedName("TotalMarks")
    private int TotalMarks;
    @SerializedName("StartDateTime")
    private String StartDateTime;
    @SerializedName("EndDateTime")
    private String EndDateTime;
    @SerializedName("Level")
    private int Level;
    @SerializedName("Label")
    private String Label;
    @SerializedName("sentFlag")
    public int sentFlag;

    public int getSentFlag() {
        return sentFlag;
    }

    public void setSentFlag(int sentFlag) {
        this.sentFlag = sentFlag;
    }

    public int getScoreId() {
        return ScoreId;
    }

    public void setScoreId(int scoreId) {
        ScoreId = scoreId;
    }

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getResourceID() {
        return ResourceID;
    }

    public void setResourceID(String resourceID) {
        ResourceID = resourceID;
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
    }

    public int getScoredMarks() {
        return ScoredMarks;
    }

    public void setScoredMarks(int scoredMarks) {
        ScoredMarks = scoredMarks;
    }

    public int getTotalMarks() {
        return TotalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        TotalMarks = totalMarks;
    }

    public String getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        StartDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return EndDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        EndDateTime = endDateTime;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

}
