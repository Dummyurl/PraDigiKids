package com.pratham.pradigikids.models;

import com.google.gson.annotations.SerializedName;

public class Modal_ProgramData {
    @SerializedName("programId")
    private
    String programId;
    @SerializedName("State")
    private
    String State;
    @SerializedName("programName")
    private
    String programName;

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }
}
