/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.EstadoProceso;
import com.ec.entidad.Solicitud;
import com.ec.entidad.Usuario;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioEstadoProceso;
import com.ec.servicio.ServicioSolicitud;

import com.ec.servicio.ServicioUsuario;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.engine.JRException;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author Darwin
 */
public class SolicitudController {

    private Usuario usuario;
    ServicioUsuario servicioUsuario = new ServicioUsuario();
    UserCredential credential = new UserCredential();
    ServicioSolicitud servicioSolicitud = new ServicioSolicitud();
    private List<Solicitud> listaDatos = new ArrayList<Solicitud>();
    private String buscar = "";

    //subir pdf
    private String filePath;
    byte[] buffer = new byte[1024 * 1024];
    private AImage fotoGeneral = null;

    ServicioEstadoProceso servicioEstadoProceso = new ServicioEstadoProceso();

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, JRException, IOException {
        Selectors.wireComponents(view, this, false);

    }

    public SolicitudController() {

        Session sess = Sessions.getCurrent();
        credential = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        buscarSolicitudes();
    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void buscarLike() {

        buscarSolicitudes();
    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void eliminarPaciente(@BindingParam("valor") Solicitud valor) {
        try {
            if (Messagebox.show("¿Esta seguro de eliminar la solicitud?", "Atención", Messagebox.YES | Messagebox.NO, Messagebox.INFORMATION) == Messagebox.YES) {

                if (valor.getIdEstadoProceso().getEstSigla().equals("APR")) {
                    Messagebox.show("No se puede eliminar la solicitud por que ya se encuentra aprobada", "Atención", Messagebox.YES, Messagebox.INFORMATION);
                } else {
                    servicioSolicitud.eliminar(valor);
                    buscarLike();
                }

            }
        } catch (Exception e) {
            Clients.showNotification("Ocurrio un error " + e.getMessage(),
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
        }
    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void nuevaSolicitud() {
        try {

            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/perfil/nuevo/solicitud.zul", null, null);
            window.doModal();
            buscarLike();
        } catch (Exception e) {
            Clients.showNotification("Ocurrio un error " + e.getMessage(),
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
        }
    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void modificarSolicitud(@BindingParam("valor") Solicitud valor) {
        try {
//            if (Messagebox.show("¿Desea modificar el registro, recuerde que debe crear las reteniones nuevamente?", "Atención", Messagebox.YES | Messagebox.NO, Messagebox.INFORMATION) == Messagebox.YES) {
            final HashMap<String, Solicitud> map = new HashMap<String, Solicitud>();

            map.put("valor", valor);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/perfil/nuevo/solicitud.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            Clients.showNotification("Ocurrio un error " + e.getMessage(),
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
        }
    }

    private void buscarSolicitudes() {
        listaDatos = servicioSolicitud.findLikeSolicitud(buscar, credential.getUsuarioSistema());

    }

    public List<Solicitud> getListaDatos() {
        return listaDatos;
    }

    public void setListaDatos(List<Solicitud> listaDatos) {
        this.listaDatos = listaDatos;
    }

    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }

    @Command
     @NotifyChange({"listaDatos", "buscar"})
    public void cancelarSolicitud(@BindingParam("valor") Solicitud valor) {

        if (Messagebox.show("Desea cambiar e", "Question", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK) {
            EstadoProceso estado = servicioEstadoProceso.findBySigla("CAN");
            valor.setIdEstadoProceso(estado);
            servicioSolicitud.modificar(valor);
        } else {
            Clients.showNotification("Solicitud cancelada",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 1000, true);
        }
    }

}
