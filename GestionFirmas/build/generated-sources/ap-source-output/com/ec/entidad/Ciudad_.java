package com.ec.entidad;

import com.ec.entidad.Provincia;
import com.ec.entidad.Solicitud;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-06-24T09:42:12")
@StaticMetamodel(Ciudad.class)
public class Ciudad_ { 

    public static volatile SingularAttribute<Ciudad, Boolean> ciuEstado;
    public static volatile SingularAttribute<Ciudad, Integer> ciuNumero;
    public static volatile SingularAttribute<Ciudad, Provincia> idProvincia;
    public static volatile CollectionAttribute<Ciudad, Solicitud> solicitudCollection;
    public static volatile SingularAttribute<Ciudad, String> ciuNombre;
    public static volatile SingularAttribute<Ciudad, Integer> idCiudad;

}