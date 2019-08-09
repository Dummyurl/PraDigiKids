package com.pratham.pradigikids.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Modal_PushData {
    @SerializedName("session")
    private
    List<Modal_PushSessionData> pushSession;
    @SerializedName("students")
    private
    List<Modal_Student> students;

    public List<Modal_PushSessionData> getPushSession() {
        return pushSession;
    }

    public void setPushSession(List<Modal_PushSessionData> pushSession) {
        this.pushSession = pushSession;
    }

    public List<Modal_Student> getStudents() {
        return students;
    }

    public void setStudents(List<Modal_Student> students) {
        this.students = students;
    }

    public class Modal_PushSessionData {
        @SerializedName("sessionid")
        String sessionId;
        @SerializedName("scores")
        List<Modal_Score> scores;
        @SerializedName("attendances")
        List<Attendance> attendances;

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public List<Modal_Score> getScores() {
            return scores;
        }

        public void setScores(List<Modal_Score> scores) {
            this.scores = scores;
        }

        public List<Attendance> getAttendances() {
            return attendances;
        }

        public void setAttendances(List<Attendance> attendances) {
            this.attendances = attendances;
        }
    }
}
