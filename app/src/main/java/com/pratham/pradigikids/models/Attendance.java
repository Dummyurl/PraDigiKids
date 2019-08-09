package com.pratham.pradigikids.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Attendance {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("AttendanceID")
    public int AttendanceID;
    @SerializedName("VillageID")
    public String VillageID;
    @SerializedName("GroupID")
    public String GroupID;
    @SerializedName("SessionID")
    public String SessionID;
    @SerializedName("StudentID")
    public String StudentID;
    @SerializedName("Date")
    public String Date;
    @SerializedName("Present")
    public int Present;
    @SerializedName("sentFlag")
    public int sentFlag;

    public int getAttendanceID() {
        return AttendanceID;
    }

    public void setAttendanceID(int attendanceID) {
        AttendanceID = attendanceID;
    }

    public String getVillageID() {
        return VillageID;
    }

    public void setVillageID(String villageID) {
        VillageID = villageID;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getPresent() {
        return Present;
    }

    public void setPresent(int present) {
        Present = present;
    }

    public int getSentFlag() {
        return sentFlag;
    }

    public void setSentFlag(int sentFlag) {
        this.sentFlag = sentFlag;
    }

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }
}
