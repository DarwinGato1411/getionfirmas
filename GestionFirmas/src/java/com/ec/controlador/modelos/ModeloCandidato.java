/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.modelos;

/**
 *
 * @author Darwin
 */
public class ModeloCandidato {
    private Integer idCandidato;
    private String usuRuc;
    private String nombre;
    private String correo;

    public ModeloCandidato(Integer idCandidato, String usuRuc, String nombre, String correo) {
        this.idCandidato = idCandidato;
        this.usuRuc = usuRuc;
        this.nombre = nombre;
        this.correo = correo;
    }

    
    public Integer getIdCandidato() {
        return idCandidato;
    }

    public void setIdCandidato(Integer idCandidato) {
        this.idCandidato = idCandidato;
    }

    public String getUsuRuc() {
        return usuRuc;
    }

    public void setUsuRuc(String usuRuc) {
        this.usuRuc = usuRuc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    
}
