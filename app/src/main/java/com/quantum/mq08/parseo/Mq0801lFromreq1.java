package com.quantum.mq08.parseo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mq0801lFromreq1 {

    @SerializedName("Descripcion")
    @Expose
    private String descripcion;
    @SerializedName("Codigo")
    @Expose
    private String codigo;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
