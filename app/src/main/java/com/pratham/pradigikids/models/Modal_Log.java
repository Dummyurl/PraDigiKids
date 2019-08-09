package com.pratham.pradigikids.models;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Logs")
public class Modal_Log {
    @PrimaryKey(autoGenerate = true)
    public int logId;
    @SerializedName("currentDateTime")
    public String currentDateTime;
    @SerializedName("exceptionMessage")
    public String exceptionMessage;
    @SerializedName("exceptionStackTrace")
    public String exceptionStackTrace;
    @SerializedName("methodName")
    public String methodName;
    @SerializedName("errorType")
    public String errorType;
    @SerializedName("sessionId")
    public String sessionId;
    @SerializedName("deviceId")
    public String deviceId;
    @SerializedName("LogDetail")
    public String LogDetail;
    @SerializedName("sentFlag")
    public int sentFlag;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionStackTrace() {
        return exceptionStackTrace;
    }

    public void setExceptionStackTrace(String exceptionStackTrace) {
        this.exceptionStackTrace = exceptionStackTrace;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getSentFlag() {
        return sentFlag;
    }

    public void setSentFlag(int sentFlag) {
        this.sentFlag = sentFlag;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLogDetail() {
        return LogDetail;
    }

    public void setLogDetail(String logDetail) {
        LogDetail = logDetail;
    }
}
