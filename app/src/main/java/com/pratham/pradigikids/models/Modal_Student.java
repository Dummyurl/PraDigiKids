package com.pratham.pradigikids.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Students")
public class Modal_Student implements Comparable, Parcelable {
    @SerializedName("GroupId")
    public String GroupId;
    @SerializedName("GroupName")
    public String GroupName;
    @SerializedName("FullName")
    public String FullName;
    @SerializedName("FirstName")
    public String FirstName;
    @SerializedName("MiddleName")
    public String MiddleName;
    @SerializedName("LastName")
    public String LastName;
    @SerializedName("Class")
    public String Stud_Class;
    @SerializedName("Age")
    public String Age;
    @SerializedName("Gender")
    public String Gender;
    @SerializedName("sentFlag")
    public int sentFlag;
    @NonNull
    @PrimaryKey
    @SerializedName("StudentId")
    public String StudentId;
    public String avatarName;
    @Ignore
    private transient boolean isChecked = false;

    private Modal_Student(Parcel in) {
        GroupId = in.readString();
        GroupName = in.readString();
        FullName = in.readString();
        FirstName = in.readString();
        MiddleName = in.readString();
        LastName = in.readString();
        Stud_Class = in.readString();
        Age = in.readString();
        Gender = in.readString();
        sentFlag = in.readInt();
        StudentId = in.readString();
        avatarName = in.readString();
        isChecked = in.readByte() != 0;
    }

    public static final Creator<Modal_Student> CREATOR = new Creator<Modal_Student>() {
        @Override
        public Modal_Student createFromParcel(Parcel in) {
            return new Modal_Student(in);
        }

        @Override
        public Modal_Student[] newArray(int size) {
            return new Modal_Student[size];
        }
    };

    public Modal_Student() {
    }

    public Modal_Student(@NonNull String sid, String sname, String qrGroupID) {
        this.StudentId = sid;
        this.FirstName = sname;
        this.GroupId = qrGroupID;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getStud_Class() {
        return Stud_Class;
    }

    public void setStud_Class(String stud_Class) {
        Stud_Class = stud_Class;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public int getSentFlag() {
        return sentFlag;
    }

    public void setSentFlag(int sentFlag) {
        this.sentFlag = sentFlag;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @NonNull
    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(@NonNull String studentId) {
        StudentId = studentId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Modal_Student compare = (Modal_Student) o;
        if (compare.isChecked() == this.isChecked())
            return 0;
        else return 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(GroupId);
        dest.writeString(GroupName);
        dest.writeString(FullName);
        dest.writeString(FirstName);
        dest.writeString(MiddleName);
        dest.writeString(LastName);
        dest.writeString(Stud_Class);
        dest.writeString(Age);
        dest.writeString(Gender);
        dest.writeInt(sentFlag);
        dest.writeString(StudentId);
        dest.writeString(avatarName);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }
}