package com.ec.entidad;

import com.ec.entidad.Solicitud;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-07-03T10:34:03")
@StaticMetamodel(EstadoProceso.class)
public class EstadoProceso_ { 

    public static volatile SingularAttribute<EstadoProceso, String> estDescripcion;
    public static volatile CollectionAttribute<EstadoProceso, Solicitud> solicitudCollection;
    public static volatile SingularAttribute<EstadoProceso, String> estSigla;
    public static volatile SingularAttribute<EstadoProceso, Integer> idEstadoProceso;

}