package com.pratham.pradigikids.models;

import com.google.gson.annotations.SerializedName;

class Modal_ProgramState {
    @SerializedName("id")
    private
    String state_id;
    @SerializedName("data")
    private
    Modal_ProgramData data;
    @SerializedName("filter_name")
    private
    String filter_name;
    @SerializedName("table_name")
    private
    String table_name;
    @SerializedName("facility")
    private
    String facility;
    @SerializedName("created_at")
    private
    String created_at;

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public Modal_ProgramData getData() {
        return data;
    }

    public void setData(Modal_ProgramData data) {
        this.data = data;
    }

    public String getFilter_name() {
        return filter_name;
    }

    public void setFilter_name(String filter_name) {
        this.filter_name = filter_name;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
