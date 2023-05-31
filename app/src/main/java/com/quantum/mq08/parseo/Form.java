package com.quantum.mq08.parseo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Form {
    @SerializedName("fs_P4108_W4108A")
    @Expose
    private FsP4108W4108A fsP4108W4108A;
    @SerializedName("stackId")
    @Expose
    private Integer stackId;
    @SerializedName("stateId")
    @Expose
    private Integer stateId;
    @SerializedName("rid")
    @Expose
    private String rid;
    @SerializedName("currentApp")
    @Expose
    private String currentApp;
    @SerializedName("timeStamp")
    @Expose
    private String timeStamp;
    @SerializedName("sysErrors")
    @Expose
    private List<Object> sysErrors;
    @SerializedName("fs_P4108_W4108C")
    @Expose
    private FsP4108W4108C fsP4108W4108C;
    @SerializedName("fs_P4108_W4108B")
    @Expose
    private FsP4108W4108B fsP4108W4108B;

    public FsP4108W4108A getFsP4108W4108A() {
        return fsP4108W4108A;
    }

    public void setFsP4108W4108A(FsP4108W4108A fsP4108W4108A) {
        this.fsP4108W4108A = fsP4108W4108A;
    }

    public Integer getStackId() {
        return stackId;
    }

    public void setStackId(Integer stackId) {
        this.stackId = stackId;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getCurrentApp() {
        return currentApp;
    }

    public void setCurrentApp(String currentApp) {
        this.currentApp = currentApp;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<Object> getSysErrors() {
        return sysErrors;
    }

    public void setSysErrors(List<Object> sysErrors) {
        this.sysErrors = sysErrors;
    }

    public FsP4108W4108C getFsP4108W4108C() {
        return fsP4108W4108C;
    }

    public void setFsP4108W4108C(FsP4108W4108C fsP4108W4108C) {
        this.fsP4108W4108C = fsP4108W4108C;
    }

    public FsP4108W4108B getFsP4108W4108B() {
        return fsP4108W4108B;
    }

    public void setFsP4108W4108B(FsP4108W4108B fsP4108W4108B) {
        this.fsP4108W4108B = fsP4108W4108B;
    }

}
