/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.consumirws;

/**
 *
 * @author Darwin
 */
public class RequestApiEmpresa {
    	private Integer idSolicitud;
	private Integer idUsuario;
     	private String clave;


	public RequestApiEmpresa() {
		super();
	}

	public RequestApiEmpresa(Integer idSolicitud, Integer idUsuario) {
		super();
		this.idSolicitud = idSolicitud;
		this.idUsuario = idUsuario;
	}

	public Integer getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(Integer idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
        
        
}
