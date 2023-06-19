/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;


import com.ec.entidad.Solicitud;
import com.ec.servicio.ServicioSolicitud;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class CambioEstadoSol {

    @Wire
    Window windowEstFact;
    private Solicitud solicitud;
    private ServicioSolicitud servicioSolicitud = new ServicioSolicitud();
    private String estadoSol;
    private String descripcionAnula;
  

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Solicitud valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        this.solicitud = valor;
        System.out.println(valor);
        estadoSol=valor.getIdEstadoProceso().getEstSigla();
    }

    @Command
    public void guardar() {
       
//            facturar.setEstadosri(estado);
//            facturar.setMensajesri(descripcionAnula);
            servicioSolicitud.modificar(solicitud);

            Clients.showNotification("Guardado correctamente",
                    Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 1000, true);
            windowEstFact.detach();
//        } else {
//            Clients.showNotification("Verifique el estado de la factura",
//                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
//            windowEstFact.detach();
//        }

    }

    public String getEstadoSol() {
        return estadoSol;
    }

    public void setEstadoSol(String estadoSol) {
        this.estadoSol = estadoSol;
    }

  
   

    public String getDescripcionAnula() {
        return descripcionAnula;
    }

    public void setDescripcionAnula(String descripcionAnula) {
        this.descripcionAnula = descripcionAnula;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

}
