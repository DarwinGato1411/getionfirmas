package com.ec.entidad;

import com.ec.entidad.Ciudad;
import com.ec.entidad.EstadoFirma;
import com.ec.entidad.EstadoProceso;
import com.ec.entidad.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-06-16T03:40:38")
@StaticMetamodel(Solicitud.class)
public class Solicitud_ { 

    public static volatile SingularAttribute<Solicitud, String> solVigencia;
    public static volatile SingularAttribute<Solicitud, String> solPathConstitucion;
    public static volatile SingularAttribute<Solicitud, String> solPathCedulaAnverso;
    public static volatile SingularAttribute<Solicitud, String> solMailOp;
    public static volatile SingularAttribute<Solicitud, String> solDireccionCompleta;
    public static volatile SingularAttribute<Solicitud, Usuario> idUsuario;
    public static volatile SingularAttribute<Solicitud, Integer> idSolicitud;
    public static volatile SingularAttribute<Solicitud, String> solMail;
    public static volatile SingularAttribute<Solicitud, String> solCelular;
    public static volatile SingularAttribute<Solicitud, Ciudad> idCiudad;
    public static volatile SingularAttribute<Solicitud, String> solNombre;
    public static volatile SingularAttribute<Solicitud, String> solSexo;
    public static volatile SingularAttribute<Solicitud, String> solRuc;
    public static volatile SingularAttribute<Solicitud, String> solCiudad;
    public static volatile SingularAttribute<Solicitud, EstadoProceso> idEstadoProceso;
    public static volatile SingularAttribute<Solicitud, String> solCodigoDactilar;
    public static volatile SingularAttribute<Solicitud, String> solPathRuc;
    public static volatile SingularAttribute<Solicitud, Boolean> ageEstado;
    public static volatile SingularAttribute<Solicitud, String> solFormato;
    public static volatile SingularAttribute<Solicitud, String> solNacionalidad;
    public static volatile SingularAttribute<Solicitud, String> solCertificado;
    public static volatile SingularAttribute<Solicitud, String> solPathCedulaReverso;
    public static volatile SingularAttribute<Solicitud, String> solJson;
    public static volatile SingularAttribute<Solicitud, String> solIdRequest;
    public static volatile SingularAttribute<Solicitud, String> solProvincia;
    public static volatile SingularAttribute<Solicitud, String> solApellido1;
    public static volatile SingularAttribute<Solicitud, String> solCelularOp;
    public static volatile SingularAttribute<Solicitud, String> solPathSelfi;
    public static volatile SingularAttribute<Solicitud, String> solApellido2;
    public static volatile SingularAttribute<Solicitud, String> solPathCedula;
    public static volatile SingularAttribute<Solicitud, EstadoFirma> idEstadoFirma;

}