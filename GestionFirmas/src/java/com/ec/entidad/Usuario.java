/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.entidad;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "usuario")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
    , @NamedQuery(name = "Usuario.findByIdUsuario", query = "SELECT u FROM Usuario u WHERE u.idUsuario = :idUsuario")
    , @NamedQuery(name = "Usuario.findByUsuRuc", query = "SELECT u FROM Usuario u WHERE u.usuRuc = :usuRuc")
    , @NamedQuery(name = "Usuario.findByUsuNombre", query = "SELECT u FROM Usuario u WHERE u.usuNombre = :usuNombre")
    , @NamedQuery(name = "Usuario.findByUsuLogin", query = "SELECT u FROM Usuario u WHERE u.usuLogin = :usuLogin")
    , @NamedQuery(name = "Usuario.findByUsuPassword", query = "SELECT u FROM Usuario u WHERE u.usuPassword = :usuPassword")
    , @NamedQuery(name = "Usuario.findByUsuCorreo", query = "SELECT u FROM Usuario u WHERE u.usuCorreo = :usuCorreo")
    , @NamedQuery(name = "Usuario.findByUsuTipoUsuario", query = "SELECT u FROM Usuario u WHERE u.usuTipoUsuario = :usuTipoUsuario")
    , @NamedQuery(name = "Usuario.findByUsuFechaRegistro", query = "SELECT u FROM Usuario u WHERE u.usuFechaRegistro = :usuFechaRegistro")
    , @NamedQuery(name = "Usuario.findByUsuActivo", query = "SELECT u FROM Usuario u WHERE u.usuActivo = :usuActivo")
    , @NamedQuery(name = "Usuario.findByUsuFoto", query = "SELECT u FROM Usuario u WHERE u.usuFoto = :usuFoto")
    , @NamedQuery(name = "Usuario.findByUsuNivel", query = "SELECT u FROM Usuario u WHERE u.usuNivel = :usuNivel")
    , @NamedQuery(name = "Usuario.findByUsuLogo", query = "SELECT u FROM Usuario u WHERE u.usuLogo = :usuLogo")
    , @NamedQuery(name = "Usuario.findByUsuEncabezadoReceta", query = "SELECT u FROM Usuario u WHERE u.usuEncabezadoReceta = :usuEncabezadoReceta")
    , @NamedQuery(name = "Usuario.findByUsuPiePaginaReceta", query = "SELECT u FROM Usuario u WHERE u.usuPiePaginaReceta = :usuPiePaginaReceta")
    , @NamedQuery(name = "Usuario.findByUsuFirma", query = "SELECT u FROM Usuario u WHERE u.usuFirma = :usuFirma")
    , @NamedQuery(name = "Usuario.findByUsuEncabezadoCert", query = "SELECT u FROM Usuario u WHERE u.usuEncabezadoCert = :usuEncabezadoCert")
    , @NamedQuery(name = "Usuario.findByUsuPiePaginaCeritifacdo", query = "SELECT u FROM Usuario u WHERE u.usuPiePaginaCeritifacdo = :usuPiePaginaCeritifacdo")
    , @NamedQuery(name = "Usuario.findByUsuCiudad", query = "SELECT u FROM Usuario u WHERE u.usuCiudad = :usuCiudad")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Column(name = "usu_ruc")
    private String usuRuc;
    @Column(name = "usu_nombre")
    private String usuNombre;
    @Column(name = "usu_login")
    private String usuLogin;
    @Column(name = "usu_password")
    private String usuPassword;
    @Column(name = "usu_correo")
    private String usuCorreo;
    @Column(name = "usu_tipo_usuario")
    private String usuTipoUsuario;
    @Column(name = "usu_fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date usuFechaRegistro;
    @Column(name = "usu_activo")
    private Boolean usuActivo;
    @Column(name = "usu_foto")
    private String usuFoto;
    @Column(name = "usu_nivel")
    private Integer usuNivel;
    @Column(name = "usu_logo")
    private String usuLogo;
    @Column(name = "usu_encabezado_receta")
    private String usuEncabezadoReceta;
    @Column(name = "usu_pie_pagina_receta")
    private String usuPiePaginaReceta;
    @Column(name = "usu_firma")
    private String usuFirma;
    @Column(name = "usu_encabezado_cert")
    private String usuEncabezadoCert;
    @Column(name = "usu_pie_pagina_ceritifacdo")
    private String usuPiePaginaCeritifacdo;
    @Column(name = "usu_ciudad")
    private String usuCiudad;
    @OneToMany(mappedBy = "idUsuario")
    private Collection<Solicitud> solicitudCollection;

    public Usuario() {
    }

    public Usuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuRuc() {
        return usuRuc;
    }

    public void setUsuRuc(String usuRuc) {
        this.usuRuc = usuRuc;
    }

    public String getUsuNombre() {
        return usuNombre;
    }

    public void setUsuNombre(String usuNombre) {
        this.usuNombre = usuNombre;
    }

    public String getUsuLogin() {
        return usuLogin;
    }

    public void setUsuLogin(String usuLogin) {
        this.usuLogin = usuLogin;
    }

    public String getUsuPassword() {
        return usuPassword;
    }

    public void setUsuPassword(String usuPassword) {
        this.usuPassword = usuPassword;
    }

    public String getUsuCorreo() {
        return usuCorreo;
    }

    public void setUsuCorreo(String usuCorreo) {
        this.usuCorreo = usuCorreo;
    }

    public String getUsuTipoUsuario() {
        return usuTipoUsuario;
    }

    public void setUsuTipoUsuario(String usuTipoUsuario) {
        this.usuTipoUsuario = usuTipoUsuario;
    }

    public Date getUsuFechaRegistro() {
        return usuFechaRegistro;
    }

    public void setUsuFechaRegistro(Date usuFechaRegistro) {
        this.usuFechaRegistro = usuFechaRegistro;
    }

    public Boolean getUsuActivo() {
        return usuActivo;
    }

    public void setUsuActivo(Boolean usuActivo) {
        this.usuActivo = usuActivo;
    }

    public String getUsuFoto() {
        return usuFoto;
    }

    public void setUsuFoto(String usuFoto) {
        this.usuFoto = usuFoto;
    }

    public Integer getUsuNivel() {
        return usuNivel;
    }

    public void setUsuNivel(Integer usuNivel) {
        this.usuNivel = usuNivel;
    }

    public String getUsuLogo() {
        return usuLogo;
    }

    public void setUsuLogo(String usuLogo) {
        this.usuLogo = usuLogo;
    }

    public String getUsuEncabezadoReceta() {
        return usuEncabezadoReceta;
    }

    public void setUsuEncabezadoReceta(String usuEncabezadoReceta) {
        this.usuEncabezadoReceta = usuEncabezadoReceta;
    }

    public String getUsuPiePaginaReceta() {
        return usuPiePaginaReceta;
    }

    public void setUsuPiePaginaReceta(String usuPiePaginaReceta) {
        this.usuPiePaginaReceta = usuPiePaginaReceta;
    }

    public String getUsuFirma() {
        return usuFirma;
    }

    public void setUsuFirma(String usuFirma) {
        this.usuFirma = usuFirma;
    }

    public String getUsuEncabezadoCert() {
        return usuEncabezadoCert;
    }

    public void setUsuEncabezadoCert(String usuEncabezadoCert) {
        this.usuEncabezadoCert = usuEncabezadoCert;
    }

    public String getUsuPiePaginaCeritifacdo() {
        return usuPiePaginaCeritifacdo;
    }

    public void setUsuPiePaginaCeritifacdo(String usuPiePaginaCeritifacdo) {
        this.usuPiePaginaCeritifacdo = usuPiePaginaCeritifacdo;
    }

    public String getUsuCiudad() {
        return usuCiudad;
    }

    public void setUsuCiudad(String usuCiudad) {
        this.usuCiudad = usuCiudad;
    }

    public Collection<Solicitud> getSolicitudCollection() {
        return solicitudCollection;
    }

    public void setSolicitudCollection(Collection<Solicitud> solicitudCollection) {
        this.solicitudCollection = solicitudCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.Usuario[ idUsuario=" + idUsuario + " ]";
    }
    
}
