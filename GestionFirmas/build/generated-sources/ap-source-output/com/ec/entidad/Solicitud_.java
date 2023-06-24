package com.ec.entidad;

import com.ec.entidad.Ciudad;
import com.ec.entidad.EstadoFirma;
import com.ec.entidad.EstadoProceso;
import com.ec.entidad.TipoFirma;
import com.ec.entidad.Usuario;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-06-24T09:36:41")
@StaticMetamodel(Solicitud.class)
public class Solicitud_ { 

    public static volatile SingularAttribute<Solicitud, String> solVigencia;
    public static volatile SingularAttribute<Solicitud, String> solDireccionCompleta;
    public static volatile SingularAttribute<Solicitud, Integer> idSolicitud;
    public static volatile SingularAttribute<Solicitud, String> solMail;
    public static volatile SingularAttribute<Solicitud, String> solCargoRepresentante;
    public static volatile SingularAttribute<Solicitud, String> solNombre;
    public static volatile SingularAttribute<Solicitud, String> solTipo;
    public static volatile SingularAttribute<Solicitud, String> solSexo;
    public static volatile SingularAttribute<Solicitud, String> solTipoDocumento;
    public static volatile SingularAttribute<Solicitud, EstadoProceso> idEstadoProceso;
    public static volatile SingularAttribute<Solicitud, String> solFormato;
    public static volatile SingularAttribute<Solicitud, String> solPathNombramientoRepresentante;
    public static volatile SingularAttribute<Solicitud, String> solNacionalidad;
    public static volatile SingularAttribute<Solicitud, String> solMeRuc;
    public static volatile SingularAttribute<Solicitud, String> solCertificado;
    public static volatile SingularAttribute<Solicitud, String> solPathAutorizacionRepresentante;
    public static volatile SingularAttribute<Solicitud, String> solMeTipodocumento;
    public static volatile SingularAttribute<Solicitud, String> solRucEmpresa;
    public static volatile SingularAttribute<Solicitud, String> solRazonSocial;
    public static volatile SingularAttribute<Solicitud, String> solIdRequest;
    public static volatile SingularAttribute<Solicitud, String> solProvincia;
    public static volatile SingularAttribute<Solicitud, String> solCelularOp;
    public static volatile SingularAttribute<Solicitud, String> solPathCedula;
    public static volatile SingularAttribute<Solicitud, String> solPathConstitucion;
    public static volatile SingularAttribute<Solicitud, String> solCargoSolicitante;
    public static volatile SingularAttribute<Solicitud, String> solPathConstitucionCompania;
    public static volatile SingularAttribute<Solicitud, String> solPathCedulaAnverso;
    public static volatile SingularAttribute<Solicitud, String> solPathAceptacionNombramiento;
    public static volatile SingularAttribute<Solicitud, Date> solFechaNacimiento;
    public static volatile SingularAttribute<Solicitud, String> solMailOp;
    public static volatile SingularAttribute<Solicitud, Usuario> idUsuario;
    public static volatile SingularAttribute<Solicitud, TipoFirma> idTipoFirma;
    public static volatile SingularAttribute<Solicitud, String> solCelular;
    public static volatile SingularAttribute<Solicitud, String> solPathCedulaRepresentanteEmpresa;
    public static volatile SingularAttribute<Solicitud, Ciudad> idCiudad;
    public static volatile SingularAttribute<Solicitud, Date> solFechaCreacion;
    public static volatile SingularAttribute<Solicitud, String> solPathRucEmpresa;
    public static volatile SingularAttribute<Solicitud, String> solArea;
    public static volatile SingularAttribute<Solicitud, String> solRuc;
    public static volatile SingularAttribute<Solicitud, Integer> solEdad;
    public static volatile SingularAttribute<Solicitud, String> solCiudad;
    public static volatile SingularAttribute<Solicitud, String> solCodigoDactilar;
    public static volatile SingularAttribute<Solicitud, String> solPathRuc;
    public static volatile SingularAttribute<Solicitud, Boolean> ageEstado;
    public static volatile SingularAttribute<Solicitud, String> solMeApellido2;
    public static volatile SingularAttribute<Solicitud, String> solMeApellido1;
    public static volatile SingularAttribute<Solicitud, Boolean> solConRuc;
    public static volatile SingularAttribute<Solicitud, String> solPathCedulaReverso;
    public static volatile SingularAttribute<Solicitud, String> solJson;
    public static volatile SingularAttribute<Solicitud, String> solApellido1;
    public static volatile SingularAttribute<Solicitud, String> solPathSelfi;
    public static volatile SingularAttribute<Solicitud, String> solApellido2;
    public static volatile SingularAttribute<Solicitud, String> solMeNombres;
    public static volatile SingularAttribute<Solicitud, EstadoFirma> idEstadoFirma;

}