package com.quantum.mq08.parseo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mq0801dDatareq {
    @SerializedName("Desposito")
    @Expose
    private String desposito;
    @SerializedName("Descripcion")
    @Expose
    private String descripcion;

    public String getDesposito() {
        return desposito;
    }

    public void setDesposito(String desposito) {
        this.desposito = desposito;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
