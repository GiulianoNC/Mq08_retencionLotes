package com.quantum.mq08.parseo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cuerpo {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("Sucursal")
    @Expose
    private String sucursal;
    @SerializedName("Lote")
    @Expose
    private String lote;
    @SerializedName("Estado")
    @Expose
    private String estado;

    public Cuerpo(String username, String password, String sucursal, String lote, String estado) {
        this.username = username;
        this.password = password;
        this.sucursal = sucursal;
        this.lote = lote;
        this.estado = estado;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    //RESPUESTA
    @SerializedName("ServiceRequest1")
    @Expose
    private ServiceRequest1 serviceRequest1;
    @SerializedName("ServiceRequest2")
    @Expose
    private ServiceRequest2 serviceRequest2;
    @SerializedName("jde__status")
    @Expose
    private String jdeStatus;
    @SerializedName("jde__startTimestamp")
    @Expose
    private String jdeStartTimestamp;
    @SerializedName("jde__endTimestamp")
    @Expose
    private String jdeEndTimestamp;
    @SerializedName("jde__serverExecutionSeconds")
    @Expose
    private Double jdeServerExecutionSeconds;

    public ServiceRequest1 getServiceRequest1() {
        return serviceRequest1;
    }

    public void setServiceRequest1(ServiceRequest1 serviceRequest1) {
        this.serviceRequest1 = serviceRequest1;
    }

    public ServiceRequest2 getServiceRequest2() {
        return serviceRequest2;
    }

    public void setServiceRequest2(ServiceRequest2 serviceRequest2) {
        this.serviceRequest2 = serviceRequest2;
    }

    public String getJdeStatus() {
        return jdeStatus;
    }

    public void setJdeStatus(String jdeStatus) {
        this.jdeStatus = jdeStatus;
    }

    public String getJdeStartTimestamp() {
        return jdeStartTimestamp;
    }

    public void setJdeStartTimestamp(String jdeStartTimestamp) {
        this.jdeStartTimestamp = jdeStartTimestamp;
    }

    public String getJdeEndTimestamp() {
        return jdeEndTimestamp;
    }

    public void setJdeEndTimestamp(String jdeEndTimestamp) {
        this.jdeEndTimestamp = jdeEndTimestamp;
    }

    public Double getJdeServerExecutionSeconds() {
        return jdeServerExecutionSeconds;
    }

    public void setJdeServerExecutionSeconds(Double jdeServerExecutionSeconds) {
        this.jdeServerExecutionSeconds = jdeServerExecutionSeconds;
    }

}