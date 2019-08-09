package com.pratham.pradigikids.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "CRL")
public class Modal_Crl {
    @NonNull
    @PrimaryKey
    @SerializedName("CRLId")
    private
    String CRLId;
    @SerializedName("RoleId")
    private
    String RoleId;
    @SerializedName("RoleName")
    private
    String RoleName;
    @SerializedName("ProgramId")
    private
    String ProgramId;
    @SerializedName("ProgramName")
    private
    String ProgramName;
    @SerializedName("State")
    private
    String State;
    @SerializedName("FirstName")
    private
    String FirstName;
    @SerializedName("LastName")
    private
    String LastName;
    @SerializedName("Mobile")
    private
    String Mobile;
    @SerializedName("Email")
    private
    String Email;
    @SerializedName("Block")
    private
    String Block;
    @SerializedName("District")
    private
    String District;
    @SerializedName("UserName")
    String UserName;
    @SerializedName("Password")
    String Password;

    @NonNull
    public String getCRLId() {
        return CRLId;
    }

    public void setCRLId(@NonNull String CRLId) {
        this.CRLId = CRLId;
    }

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public String getProgramId() {
        return ProgramId;
    }

    public void setProgramId(String programId) {
        ProgramId = programId;
    }

    public String getProgramName() {
        return ProgramName;
    }

    public void setProgramName(String programName) {
        ProgramName = programName;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
