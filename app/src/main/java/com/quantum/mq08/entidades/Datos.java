package com.quantum.mq08.entidades;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datos  {
    @SerializedName("deposito")
    @Expose
    private String deposito;

    @SerializedName("retencion")
    @Expose
    private String retencion;

    @SerializedName("item")
    @Expose
    private String item;

    @SerializedName("lote")
    @Expose
    private String lote;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("resultado")
    @Expose
    private String resultado;

    public String getDeposito() {
        return deposito;
    }

    public String setDeposito(String deposito) {
        this.deposito = deposito;
        return deposito;
    }

    public String getRetencion() {
        return retencion;
    }

    public String setRetencion(String retencion) {
        this.retencion = retencion;
        return retencion;
    }

    public String getItem() {
        return item;
    }

    public String setItem(String item) {
        this.item = item;
        return item;
    }

    public String getLote() {
        return lote;
    }

    public String setLote(String lote) {
        this.lote = lote;
        return lote;
    }

    public Integer getId() {
        return id;
    }

    public int setId(int id) {
        this.id = id;
        return id;
    }

    public String getResultado() {
        return resultado;
    }

    public String setResultado(String resultado) {
        this.resultado = resultado;
        return resultado;
    }


}
