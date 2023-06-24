package com.ec.entidad;

import com.ec.entidad.TipoFirma;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-06-24T09:36:41")
@StaticMetamodel(DetalleTipoFirma.class)
public class DetalleTipoFirma_ { 

    public static volatile SingularAttribute<DetalleTipoFirma, BigDecimal> detPrecioCompra;
    public static volatile SingularAttribute<DetalleTipoFirma, BigDecimal> detPrecioVenta;
    public static volatile SingularAttribute<DetalleTipoFirma, Boolean> detEstado;
    public static volatile SingularAttribute<DetalleTipoFirma, Integer> idDetalleTipoFirma;
    public static volatile SingularAttribute<DetalleTipoFirma, TipoFirma> idTipoFirma;

}