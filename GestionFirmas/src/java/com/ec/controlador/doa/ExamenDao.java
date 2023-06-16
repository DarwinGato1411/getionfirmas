/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.doa;

import org.zkoss.image.AImage;

/**
 *
 * @author Darwin
 */
public class ExamenDao {

    private String path;
    private String descripcion;
     private AImage fotoGeneral;

    public ExamenDao() {
    }

    public ExamenDao(String path, String descripcion) {
        this.path = path;
        this.descripcion = descripcion;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public AImage getFotoGeneral() {
        return fotoGeneral;
    }

    public void setFotoGeneral(AImage fotoGeneral) {
        this.fotoGeneral = fotoGeneral;
    }

}
