package com.ec.entidad;

import com.ec.entidad.Solicitud;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-06-28T08:50:58")
@StaticMetamodel(EstadoFirma.class)
public class EstadoFirma_ { 

    public static volatile SingularAttribute<EstadoFirma, String> estDescripcion;
    public static volatile CollectionAttribute<EstadoFirma, Solicitud> solicitudCollection;
    public static volatile SingularAttribute<EstadoFirma, String> estSigla;
    public static volatile SingularAttribute<EstadoFirma, Integer> idEstadoFirma;

}