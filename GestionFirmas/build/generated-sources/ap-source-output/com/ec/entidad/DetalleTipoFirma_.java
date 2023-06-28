package com.ec.entidad;

import com.ec.entidad.Solicitud;
import com.ec.entidad.TipoFirma;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-06-28T14:49:52")
@StaticMetamodel(DetalleTipoFirma.class)
public class DetalleTipoFirma_ { 

    public static volatile SingularAttribute<DetalleTipoFirma, String> detDescripcion;
    public static volatile SingularAttribute<DetalleTipoFirma, BigDecimal> detPrecioCompra;
    public static volatile SingularAttribute<DetalleTipoFirma, BigDecimal> detPrecioVenta;
    public static volatile CollectionAttribute<DetalleTipoFirma, Solicitud> solicitudCollection;
    public static volatile SingularAttribute<DetalleTipoFirma, Boolean> detEstado;
    public static volatile SingularAttribute<DetalleTipoFirma, Integer> idDetalleTipoFirma;
    public static volatile SingularAttribute<DetalleTipoFirma, TipoFirma> idTipoFirma;

}