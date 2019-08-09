package com.pratham.pradigikids.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Village {
    @PrimaryKey
    @SerializedName("VillageId")
    private int VillageId;
    @SerializedName("VillageCode")
    private String VillageCode;
    @SerializedName("VillageName")
    private String VillageName;
    @SerializedName("Block")
    private String Block;
    @SerializedName("District")
    private String District;
    @SerializedName("State")
    private String State;
    @SerializedName("CRLId")
    private String CRLId;

    public Village(int villageId, String villageName) {
        this.VillageId = villageId;
        this.VillageName = villageName;
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
