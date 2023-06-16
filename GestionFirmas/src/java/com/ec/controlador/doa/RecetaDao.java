/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.doa;

/**
 *
 * @author Darwin
 */
public class RecetaDao {

    private String medicamento;
    private String indicacion;
    private String recCantidad;

    private Boolean recM;

    private Boolean recT;

    private Boolean recN;

    public RecetaDao() {
    }

    public RecetaDao(String medicamento, String indicacion) {
        this.medicamento = medicamento;
        this.indicacion = indicacion;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getIndicacion() {
        return indicacion;
    }

    public void setIndicacion(String indicacion) {
        this.indicacion = indicacion;
    }

    public Boolean getRecT() {
        return recT;
    }

    public void setRecT(Boolean recT) {
        this.recT = recT;
    }

    public Boolean getRecN() {
        return recN;
    }

    public void setRecN(Boolean recN) {
        this.recN = recN;
    }

    public String getRecCantidad() {
        return recCantidad;
    }

    public void setRecCantidad(String recCantidad) {
        this.recCantidad = recCantidad;
    }

    public Boolean getRecM() {
        return recM;
    }

    public void setRecM(Boolean recM) {
        this.recM = recM;
    }

}
