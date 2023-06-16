/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.entidad;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Darwin
 */
@Entity
@Table(name = "solicitud")
@NamedQueries({
    @NamedQuery(name = "Solicitud.findAll", query = "SELECT s FROM Solicitud s")
    , @NamedQuery(name = "Solicitud.findByIdSolicitud", query = "SELECT s FROM Solicitud s WHERE s.idSolicitud = :idSolicitud")
    , @NamedQuery(name = "Solicitud.findBySolRuc", query = "SELECT s FROM Solicitud s WHERE s.solRuc = :solRuc")
    , @NamedQuery(name = "Solicitud.findBySolCodigoDactilar", query = "SELECT s FROM Solicitud s WHERE s.solCodigoDactilar = :solCodigoDactilar")
    , @NamedQuery(name = "Solicitud.findBySolNombre", query = "SELECT s FROM Solicitud s WHERE s.solNombre = :solNombre")
    , @NamedQuery(name = "Solicitud.findBySolApellido1", query = "SELECT s FROM Solicitud s WHERE s.solApellido1 = :solApellido1")
    , @NamedQuery(name = "Solicitud.findBySolApellido2", query = "SELECT s FROM Solicitud s WHERE s.solApellido2 = :solApellido2")
    , @NamedQuery(name = "Solicitud.findBySolNacionalidad", query = "SELECT s FROM Solicitud s WHERE s.solNacionalidad = :solNacionalidad")
    , @NamedQuery(name = "Solicitud.findBySolSexo", query = "SELECT s FROM Solicitud s WHERE s.solSexo = :solSexo")
    , @NamedQuery(name = "Solicitud.findBySolCelular", query = "SELECT s FROM Solicitud s WHERE s.solCelular = :solCelular")
    , @NamedQuery(name = "Solicitud.findBySolMail", query = "SELECT s FROM Solicitud s WHERE s.solMail = :solMail")
    , @NamedQuery(name = "Solicitud.findBySolCelularOp", query = "SELECT s FROM Solicitud s WHERE s.solCelularOp = :solCelularOp")
    , @NamedQuery(name = "Solicitud.findBySolMailOp", query = "SELECT s FROM Solicitud s WHERE s.solMailOp = :solMailOp")
    , @NamedQuery(name = "Solicitud.findBySolProvincia", query = "SELECT s FROM Solicitud s WHERE s.solProvincia = :solProvincia")
    , @NamedQuery(name = "Solicitud.findBySolCiudad", query = "SELECT s FROM Solicitud s WHERE s.solCiudad = :solCiudad")
    , @NamedQuery(name = "Solicitud.findBySolDireccionCompleta", query = "SELECT s FROM Solicitud s WHERE s.solDireccionCompleta = :solDireccionCompleta")
    , @NamedQuery(name = "Solicitud.findBySolFormato", query = "SELECT s FROM Solicitud s WHERE s.solFormato = :solFormato")
    , @NamedQuery(name = "Solicitud.findBySolVigencia", query = "SELECT s FROM Solicitud s WHERE s.solVigencia = :solVigencia")
    , @NamedQuery(name = "Solicitud.findBySolPathCedula", query = "SELECT s FROM Solicitud s WHERE s.solPathCedula = :solPathCedula")
    , @NamedQuery(name = "Solicitud.findBySolPathSelfi", query = "SELECT s FROM Solicitud s WHERE s.solPathSelfi = :solPathSelfi")
    , @NamedQuery(name = "Solicitud.findBySolPathRuc", query = "SELECT s FROM Solicitud s WHERE s.solPathRuc = :solPathRuc")
    , @NamedQuery(name = "Solicitud.findBySolPathConstitucion", query = "SELECT s FROM Solicitud s WHERE s.solPathConstitucion = :solPathConstitucion")
    , @NamedQuery(name = "Solicitud.findBySolPathCedulaAnverso", query = "SELECT s FROM Solicitud s WHERE s.solPathCedulaAnverso = :solPathCedulaAnverso")
    , @NamedQuery(name = "Solicitud.findBySolPathCedulaReverso", query = "SELECT s FROM Solicitud s WHERE s.solPathCedulaReverso = :solPathCedulaReverso")
    , @NamedQuery(name = "Solicitud.findByAgeEstado", query = "SELECT s FROM Solicitud s WHERE s.ageEstado = :ageEstado")
    , @NamedQuery(name = "Solicitud.findBySolCertificado", query = "SELECT s FROM Solicitud s WHERE s.solCertificado = :solCertificado")
    , @NamedQuery(name = "Solicitud.findBySolJson", query = "SELECT s FROM Solicitud s WHERE s.solJson = :solJson")
    , @NamedQuery(name = "Solicitud.findBySolIdRequest", query = "SELECT s FROM Solicitud s WHERE s.solIdRequest = :solIdRequest")})
public class Solicitud implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;
    @Column(name = "sol_ruc")
    private String solRuc;
    @Column(name = "sol_codigo_dactilar")
    private String solCodigoDactilar;
    @Column(name = "sol_nombre")
    private String solNombre;
    @Column(name = "sol_apellido1")
    private String solApellido1;
    @Column(name = "sol_apellido2")
    private String solApellido2;
    @Column(name = "sol_nacionalidad")
    private String solNacionalidad;
    @Column(name = "sol_fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date solFechaNacimiento;
    @Column(name = "sol_edad")
    private Integer solEdad;
    @Column(name = "sol_sexo")
    private String solSexo;
    @Column(name = "sol_tipo_documento")
    private String solTipoDocumento;
    @Column(name = "sol_celular")
    private String solCelular;
    @Column(name = "sol_mail")
    private String solMail;
    @Column(name = "sol_celular_op")
    private String solCelularOp;
    @Column(name = "sol_mail_op")
    private String solMailOp;
    @Column(name = "sol_provincia")
    private String solProvincia;
    @Column(name = "sol_ciudad")
    private String solCiudad;
    @Column(name = "sol_direccion_completa")
    private String solDireccionCompleta;
    @Column(name = "sol_formato")
    private String solFormato;
    @Column(name = "sol_vigencia")
    private String solVigencia;
    @Column(name = "sol_path_cedula")
    private String solPathCedula;
    @Column(name = "sol_path_selfi")
    private String solPathSelfi;
    @Column(name = "sol_path_ruc")
    private String solPathRuc;
    @Column(name = "sol_path_constitucion")
    private String solPathConstitucion;
    @Column(name = "sol_path_cedula_anverso")
    private String solPathCedulaAnverso;
    @Column(name = "sol_path_cedula_reverso")
    private String solPathCedulaReverso;
    @Column(name = "age_estado")
    private Boolean ageEstado;
    @Column(name = "sol_con_ruc")
    private Boolean solConRuc;
    @Column(name = "sol_certificado")
    private String solCertificado;
    @Column(name = "sol_json")
    private String solJson;
    @Column(name = "sol_id_request")
    private String solIdRequest;
    @JoinColumn(name = "id_ciudad", referencedColumnName = "id_ciudad")
    @ManyToOne
    private Ciudad idCiudad;
    @JoinColumn(name = "id_estado_firma", referencedColumnName = "id_estado_firma")
    @ManyToOne
    private EstadoFirma idEstadoFirma;
    @JoinColumn(name = "id_estado_proceso", referencedColumnName = "id_estado_proceso")
    @ManyToOne
    private EstadoProceso idEstadoProceso;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuario;

    public Solicitud() {
    }

    public Solicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getSolRuc() {
        return solRuc;
    }

    public void setSolRuc(String solRuc) {
        this.solRuc = solRuc;
    }

    public String getSolCodigoDactilar() {
        return solCodigoDactilar;
    }

    public void setSolCodigoDactilar(String solCodigoDactilar) {
        this.solCodigoDactilar = solCodigoDactilar;
    }

    public String getSolNombre() {
        return solNombre;
    }

    public void setSolNombre(String solNombre) {
        this.solNombre = solNombre;
    }

    public String getSolApellido1() {
        return solApellido1;
    }

    public void setSolApellido1(String solApellido1) {
        this.solApellido1 = solApellido1;
    }

    public String getSolApellido2() {
        return solApellido2;
    }

    public void setSolApellido2(String solApellido2) {
        this.solApellido2 = solApellido2;
    }

    public String getSolNacionalidad() {
        return solNacionalidad;
    }

    public void setSolNacionalidad(String solNacionalidad) {
        this.solNacionalidad = solNacionalidad;
    }

    public String getSolSexo() {
        return solSexo;
    }

    public void setSolSexo(String solSexo) {
        this.solSexo = solSexo;
    }

    public String getSolCelular() {
        return solCelular;
    }

    public void setSolCelular(String solCelular) {
        this.solCelular = solCelular;
    }

    public String getSolMail() {
        return solMail;
    }

    public void setSolMail(String solMail) {
        this.solMail = solMail;
    }

    public String getSolCelularOp() {
        return solCelularOp;
    }

    public void setSolCelularOp(String solCelularOp) {
        this.solCelularOp = solCelularOp;
    }

    public String getSolMailOp() {
        return solMailOp;
    }

    public void setSolMailOp(String solMailOp) {
        this.solMailOp = solMailOp;
    }

    public String getSolProvincia() {
        return solProvincia;
    }

    public void setSolProvincia(String solProvincia) {
        this.solProvincia = solProvincia;
    }

    public String getSolCiudad() {
        return solCiudad;
    }

    public void setSolCiudad(String solCiudad) {
        this.solCiudad = solCiudad;
    }

    public String getSolDireccionCompleta() {
        return solDireccionCompleta;
    }

    public void setSolDireccionCompleta(String solDireccionCompleta) {
        this.solDireccionCompleta = solDireccionCompleta;
    }

    public String getSolFormato() {
        return solFormato;
    }

    public void setSolFormato(String solFormato) {
        this.solFormato = solFormato;
    }

    public String getSolVigencia() {
        return solVigencia;
    }

    public void setSolVigencia(String solVigencia) {
        this.solVigencia = solVigencia;
    }

    public String getSolPathCedula() {
        return solPathCedula;
    }

    public void setSolPathCedula(String solPathCedula) {
        this.solPathCedula = solPathCedula;
    }

    public String getSolPathSelfi() {
        return solPathSelfi;
    }

    public void setSolPathSelfi(String solPathSelfi) {
        this.solPathSelfi = solPathSelfi;
    }

    public String getSolPathRuc() {
        return solPathRuc;
    }

    public void setSolPathRuc(String solPathRuc) {
        this.solPathRuc = solPathRuc;
    }

    public String getSolPathConstitucion() {
        return solPathConstitucion;
    }

    public void setSolPathConstitucion(String solPathConstitucion) {
        this.solPathConstitucion = solPathConstitucion;
    }

    public String getSolPathCedulaAnverso() {
        return solPathCedulaAnverso;
    }

    public void setSolPathCedulaAnverso(String solPathCedulaAnverso) {
        this.solPathCedulaAnverso = solPathCedulaAnverso;
    }

    public String getSolPathCedulaReverso() {
        return solPathCedulaReverso;
    }

    public void setSolPathCedulaReverso(String solPathCedulaReverso) {
        this.solPathCedulaReverso = solPathCedulaReverso;
    }

    public Boolean getAgeEstado() {
        return ageEstado;
    }

    public void setAgeEstado(Boolean ageEstado) {
        this.ageEstado = ageEstado;
    }

    public String getSolCertificado() {
        return solCertificado;
    }

    public void setSolCertificado(String solCertificado) {
        this.solCertificado = solCertificado;
    }

    public String getSolJson() {
        return solJson;
    }

    public void setSolJson(String solJson) {
        this.solJson = solJson;
    }

    public String getSolIdRequest() {
        return solIdRequest;
    }

    public void setSolIdRequest(String solIdRequest) {
        this.solIdRequest = solIdRequest;
    }

    public Ciudad getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Ciudad idCiudad) {
        this.idCiudad = idCiudad;
    }

    public EstadoFirma getIdEstadoFirma() {
        return idEstadoFirma;
    }

    public void setIdEstadoFirma(EstadoFirma idEstadoFirma) {
        this.idEstadoFirma = idEstadoFirma;
    }

    public EstadoProceso getIdEstadoProceso() {
        return idEstadoProceso;
    }

    public void setIdEstadoProceso(EstadoProceso idEstadoProceso) {
        this.idEstadoProceso = idEstadoProceso;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getSolFechaNacimiento() {
        return solFechaNacimiento;
    }

    public void setSolFechaNacimiento(Date solFechaNacimiento) {
        this.solFechaNacimiento = solFechaNacimiento;
    }

    public Integer getSolEdad() {
        return solEdad;
    }

    public void setSolEdad(Integer solEdad) {
        this.solEdad = solEdad;
    }

    public String getSolTipoDocumento() {
        return solTipoDocumento;
    }

    public void setSolTipoDocumento(String solTipoDocumento) {
        this.solTipoDocumento = solTipoDocumento;
    }

    public Boolean getSolConRuc() {
        return solConRuc;
    }

    public void setSolConRuc(Boolean solConRuc) {
        this.solConRuc = solConRuc;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSolicitud != null ? idSolicitud.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Solicitud)) {
            return false;
        }
        Solicitud other = (Solicitud) object;
        if ((this.idSolicitud == null && other.idSolicitud != null) || (this.idSolicitud != null && !this.idSolicitud.equals(other.idSolicitud))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ec.entidad.Solicitud[ idSolicitud=" + idSolicitud + " ]";
    }

}
