/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.EstadoProceso;
import com.ec.entidad.Solicitud;
import com.ec.servicio.ServicioEstadoProceso;
import com.ec.servicio.ServicioSolicitud;
import java.util.ArrayList;
import java.util.List;
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

    private EstadoProceso estadoSol;
    private List<EstadoProceso> listaEstadoProceso = new ArrayList<EstadoProceso>();
    private ServicioEstadoProceso servicioEstadoProceso = new ServicioEstadoProceso();

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Solicitud valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        this.solicitud = valor;
        System.out.println(valor);
        estadoSol = valor.getIdEstadoProceso();
        listaEstadoProceso = servicioEstadoProceso.finAll();
    }

    @Command
    public void guardar() {

//        if (solicitud.getIdEstadoProceso().equals("ING")) {
//            solicitud.getIdEstadoProceso().setIdEstadoProceso(1);
//        }
//
//        if (solicitud.getIdEstadoProceso().equals("APR")) {
//            solicitud.getIdEstadoProceso().setIdEstadoProceso(2);
//        }
//
//        if (solicitud.getIdEstadoProceso().equals("REC")) {
//            solicitud.getIdEstadoProceso().setIdEstadoProceso(3);
//        }
//
//        if (solicitud.getIdEstadoProceso().equals("CAN")) {
//            solicitud.getIdEstadoProceso().setIdEstadoProceso(4);
        // }
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

    public EstadoProceso getEstadoSol() {
        return estadoSol;
    }

    public void setEstadoSol(EstadoProceso estadoSol) {
        this.estadoSol = estadoSol;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public List<EstadoProceso> getListaEstadoProceso() {
        return listaEstadoProceso;
    }

    public void setListaEstadoProceso(List<EstadoProceso> listaEstadoProceso) {
        this.listaEstadoProceso = listaEstadoProceso;
    }

}
