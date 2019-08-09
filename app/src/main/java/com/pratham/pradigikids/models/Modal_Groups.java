package com.pratham.pradigikids.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Groups")
public class Modal_Groups implements Comparable {
    @NonNull
    @PrimaryKey
    @SerializedName("GroupId")
    public String GroupId;
    @SerializedName("GroupName")
    public String GroupName;
    @SerializedName("VillageId")
    public String VillageId;
    @SerializedName("ProgramId")
    public int ProgramId;
    @SerializedName("GroupCode")
    public String GroupCode;
    @SerializedName("SchoolName")
    public String SchoolName;
    @SerializedName("VIllageName")
    public String VIllageName;
    @SerializedName("DeviceId")
    public String DeviceId;
    @Ignore
    private
    boolean isSelected = false;

    public Modal_Groups() {
    }

    public Modal_Groups(String gid, String gname) {
        this.GroupId = gid;
        this.GroupName = gname;
    }

    @NonNull
    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(@NonNull String groupId) {
        GroupId = groupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getVillageId() {
        return VillageId;
    }

    public void setVillageId(String villageId) {
        VillageId = villageId;
    }

    public int getProgramId() {
        return ProgramId;
    }

    public void setProgramId(int programId) {
        ProgramId = programId;
    }

    public String getGroupCode() {
        return GroupCode;
    }

    public void setGroupCode(String groupCode) {
        GroupCode = groupCode;
    }

    public String getSchoolName() {
        return SchoolName;
    }

    public void setSchoolName(String schoolName) {
        SchoolName = schoolName;
    }

    public String getVIllageName() {
        return VIllageName;
    }

    public void setVIllageName(String VIllageName) {
        this.VIllageName = VIllageName;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int compareTo(Object o) {
        Modal_Groups compare = (Modal_Groups) o;
        if (compare.getGroupId().equals(this.GroupId) && compare.isSelected() == this.isSelected)
            return 0;
        else return 1;
    }
}

