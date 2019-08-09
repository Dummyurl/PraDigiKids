package com.pratham.pradigikids.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Status")
public class Modal_Status {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "statusKey")
    public String statusKey;
    @NonNull
    @ColumnInfo(name = "value")
    public String value = "";
    @ColumnInfo(name = "description")
    public String description;

    @NonNull
    public String getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(String statusKey) {
        this.statusKey = statusKey;
    }

    @NonNull
    public String getValue() {
        return value;
    }

    public void setValue(@NonNull String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}