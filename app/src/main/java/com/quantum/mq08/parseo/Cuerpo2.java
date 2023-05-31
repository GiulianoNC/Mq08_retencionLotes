package com.quantum.mq08.parseo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cuerpo2 {

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

    public Cuerpo2(String username, String password, String sucursal, String lote, String estado) {
        this.username = username;
        this.password = password;
        this.sucursal = sucursal;
        this.lote = lote;
        this.estado = estado;
    }

    public Cuerpo2(String username, String password) {
        this.username = username;
        this.password = password;
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

    @SerializedName("MQ0801D_DATAREQ")
    @Expose
    private List<Mq0801dDatareq> mq0801dDatareq;
    @SerializedName("jde__status")
    @Expose
    private String jdeStatus;

    public List<Mq0801dDatareq> getMq0801dDatareq() {
        return mq0801dDatareq;
    }

    public void setMq0801dDatareq(List<Mq0801dDatareq> mq0801dDatareq) {
        this.mq0801dDatareq = mq0801dDatareq;
    }

    public String getJdeStatus() {
        return jdeStatus;
    }

    public void setJdeStatus(String jdeStatus) {
        this.jdeStatus = jdeStatus;
    }

    @SerializedName("MQ0801L_FROMREQ_1")
    @Expose
    private List<Mq0801lFromreq1> mq0801lFromreq1;


    public List<Mq0801lFromreq1> getMq0801lFromreq1() {
        return mq0801lFromreq1;
    }

    public void setMq0801lFromreq1(List<Mq0801lFromreq1> mq0801lFromreq1) {
        this.mq0801lFromreq1 = mq0801lFromreq1;
    }

}
