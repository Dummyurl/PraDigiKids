package com.pratham.pradigikids.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Village")
public class Modal_Village {
    @PrimaryKey
    @SerializedName("VillageId")
    public int VillageId;
    @SerializedName("VillageCode")
    public String VillageCode;
    @SerializedName("VillageName")
    public String VillageName;
    @SerializedName("Block")
    public String Block;
    @SerializedName("District")
    public String District;
    @SerializedName("State")
    public String State;
    @SerializedName("CRLId")
    public String CRLId;

    public Modal_Village() {
    }

    Modal_Village(int id) {
        VillageId = id;
    }


    public Modal_Village(int vid, String vname) {
        this.VillageId = vid;
        this.VillageName = vname;
    }

    public int getVillageId() {
        return VillageId;
    }

    public void setVillageId(int villageId) {
        VillageId = villageId;
    }

    public String getVillageCode() {
        return VillageCode;
    }

    public void setVillageCode(String villageCode) {
        VillageCode = villageCode;
    }

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }

    public String getBlock() {
        return Block;
    }

    public void setBlock(String block) {
        Block = block;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCRLId() {
        return CRLId;
    }

    public void setCRLId(String CRLId) {
        this.CRLId = CRLId;
    }
}
