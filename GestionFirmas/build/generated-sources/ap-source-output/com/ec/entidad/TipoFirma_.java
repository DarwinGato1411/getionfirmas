package com.ec.entidad;

import com.ec.entidad.DetalleTipoFirma;
import com.ec.entidad.Solicitud;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-06-24T09:42:12")
@StaticMetamodel(TipoFirma.class)
public class TipoFirma_ { 

    public static volatile SingularAttribute<TipoFirma, String> tipEstado;
    public static volatile CollectionAttribute<TipoFirma, Solicitud> solicitudCollection;
    public static volatile SingularAttribute<TipoFirma, Integer> idTipoFirma;
    public static volatile CollectionAttribute<TipoFirma, DetalleTipoFirma> detalleTipoFirmaCollection;
    public static volatile SingularAttribute<TipoFirma, String> tipDescripcion;

}