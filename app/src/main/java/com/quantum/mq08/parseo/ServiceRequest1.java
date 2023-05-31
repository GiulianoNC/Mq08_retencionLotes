package com.quantum.mq08.parseo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceRequest1 {
    @SerializedName("submitted")
    @Expose
    private Boolean submitted;
    @SerializedName("output")
    @Expose
    private Output output;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("errorList")
    @Expose
    private List<Errores> errorList;
    @SerializedName("diagnostics")
    @Expose
    private String diagnostics;

    public Boolean getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Boolean submitted) {
        this.submitted = submitted;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Errores> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<Errores> errorList) {
        this.errorList = errorList;
    }

    public String getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(String diagnostics) {
        this.diagnostics = diagnostics;
    }
}
