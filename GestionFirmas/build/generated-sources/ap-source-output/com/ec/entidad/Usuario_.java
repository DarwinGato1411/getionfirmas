package com.ec.entidad;

import com.ec.entidad.Solicitud;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-06-28T14:49:52")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, String> usuCorreo;
    public static volatile SingularAttribute<Usuario, String> usuFirma;
    public static volatile CollectionAttribute<Usuario, Solicitud> solicitudCollection;
    public static volatile SingularAttribute<Usuario, Integer> idUsuario;
    public static volatile SingularAttribute<Usuario, String> usuLogo;
    public static volatile SingularAttribute<Usuario, String> usuPiePaginaReceta;
    public static volatile SingularAttribute<Usuario, String> usuNombre;
    public static volatile SingularAttribute<Usuario, String> usuLogin;
    public static volatile SingularAttribute<Usuario, Integer> usuNivel;
    public static volatile SingularAttribute<Usuario, String> usuFoto;
    public static volatile SingularAttribute<Usuario, String> usuCiudad;
    public static volatile SingularAttribute<Usuario, String> usuTipoUsuario;
    public static volatile SingularAttribute<Usuario, Date> usuFechaRegistro;
    public static volatile SingularAttribute<Usuario, String> usuPiePaginaCeritifacdo;
    public static volatile SingularAttribute<Usuario, String> usuRuc;
    public static volatile SingularAttribute<Usuario, String> usuPassword;
    public static volatile SingularAttribute<Usuario, Boolean> usuActivo;
    public static volatile SingularAttribute<Usuario, String> usuEncabezadoCert;
    public static volatile SingularAttribute<Usuario, String> usuEncabezadoReceta;

}