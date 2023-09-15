/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.EstadoFirma;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.zkoss.zk.au.out.AuScript;
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
    private Date fechainicio = new Date();
    private Date fechafin = new Date();

    //subir pdf
    private String filePath;
    byte[] buffer = new byte[1024 * 1024];
    private AImage fotoGeneral = null;

    private String divClass = "claseModZK";

    ServicioEstadoProceso servicioEstadoProceso = new ServicioEstadoProceso();

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, JRException, IOException {
        Selectors.wireComponents(view, this, false);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechainicio);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        fechainicio = calendar.getTime();
        fechainicio = fechaFormateada("inicio", fechainicio);
        fechafin = fechaFormateada("fin", fechafin);

        System.out.println(fechafin);
        System.out.println(fechainicio);
        buscarSolicitudes();
    }

    public SolicitudController() {

        Session sess = Sessions.getCurrent();
        credential = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        buscarSolicitudes();
    }

    @Command
    @NotifyChange("divClass")
    public void cambiarClase() {
        String claseNueva = "asdasd";
        if (!divClass.contains(claseNueva)) {
            divClass += " " + claseNueva;
        }

    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void buscarLike() {
        listaDatos = servicioSolicitud.findLikeSolicitud(buscar, credential.getUsuarioSistema());
        //buscarSolicitudes();
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

            if (valor.getIdEstadoFirma().getEstSigla().equals("EMT")
                    || valor.getIdEstadoProceso().getEstSigla().equals("APR")
                    || valor.getIdEstadoProceso().getEstSigla().equals("REC")
                    || valor.getIdEstadoProceso().getEstSigla().equals("CAN")) {
                org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/perfil/nuevo/solicitudNE.zul", null, map);
                window.doModal();
            } else {
                org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/perfil/nuevo/solicitud.zul", null, map);
                window.doModal();
            }

        } catch (Exception e) {
            Clients.showNotification("Ocurrio un error " + e.getMessage(),
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
        }
    }

    private void buscarSolicitudes() {
        listaDatos = servicioSolicitud.findLikeSolicitud(buscar, credential.getUsuarioSistema());

        //listaDatos = servicioSolicitud.findSolicitudFecha(fechainicio, fechafin, credential.getUsuarioSistema());
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

        if (Messagebox.show("Desea cancelar la solicitud?", "Question", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK) {
            EstadoProceso estado = servicioEstadoProceso.findBySigla("CAN");
            valor.setIdEstadoProceso(estado);
            servicioSolicitud.modificar(valor);
        } else {
            Clients.showNotification("Solicitud cancelada",
                    Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 1000, true);
        }
    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void elimiarSolicitud(@BindingParam("valor") Solicitud valor) {

        if (Messagebox.show("Desea cancelar la solicitud?", "Question", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK) {
            if (valor.getIdEstadoProceso().getEstSigla().equals("APR")) {
                Clients.showNotification("No se puede eliminar la solicitud ya ha sido aporbada",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 1000, true);
            } else {
                servicioSolicitud.eliminar(valor);
                buscarLike();
                Clients.showNotification("Solicitud eliminada con éxito",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 1000, true);
            }

        } else {
            Clients.showNotification("Solicitud cancelada",
                    Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 1000, true);
        }
    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void duplicarSolicitud(@BindingParam("valor") Solicitud valor) {

        if (Messagebox.show("Desea duplicar la solicitud?", "Question", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION) == Messagebox.OK) {
            Solicitud nuevaSolicitud = new Solicitud();
            nuevaSolicitud = valor;
            nuevaSolicitud.setIdEstadoProceso(servicioEstadoProceso.findBySigla("ING"));
            nuevaSolicitud.setSolFechaCreacion(new Date());
            EstadoFirma idEstadoFirma = new EstadoFirma();
            idEstadoFirma.setIdEstadoFirma(3);
            nuevaSolicitud.setIdEstadoFirma(idEstadoFirma);
            servicioSolicitud.crear(valor);
            sweetAltert("success", "OK", "Solicitud duplicada con éxito");
            Clients.response(new AuScript("setTimeout(function() { window.location.href = 'solicitud.zul'; }, 1000);"));
        } else {
            Clients.showNotification("Solicitud cancelada",
                    Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 1000, true);
        }
    }

    public void sweetAltert(String alertaTipo, String tituloMensaje, String detalleMensaje) {
        String script = "Swal.fire(\n"
                + "  '" + tituloMensaje + "',\n"
                + "  '" + detalleMensaje + "',\n"
                + "  '" + alertaTipo + "'\n"
                + ")";
        System.out.println(script);
        Clients.evalJavaScript(script);
    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void buscarFechas(@BindingParam("valor") Solicitud valor) {
        fechainicio = fechaFormateada("inicio", fechainicio);
        fechafin = fechaFormateada("fin", fechafin);

        System.out.println(fechainicio);
        System.out.println(fechafin);
        listaDatos = servicioSolicitud.findSolicitudFecha(fechainicio, fechafin, credential.getUsuarioSistema());
    }

    public Date fechaFormateada(String tipo, Date fecha) {
        Date fechaActual = fecha;

        // Crear un objeto SimpleDateFormat para el formato de la hora
        String horaEspecificaStr = "00:00:00";
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");

        if (tipo.equals("fin")) {
            horaEspecificaStr = "23:59:59";
        }

        try {
            // Obtener la hora específica como una cadena

            // Obtener la fecha actual como una cadena en formato "yyyy-MM-dd"
            String fechaActualStr = new SimpleDateFormat("yyyy-MM-dd").format(fechaActual);

            // Concatenar la fecha y la hora
            String fechaHoraConcatenadaStr = fechaActualStr + " " + horaEspecificaStr;

            // Crear un objeto SimpleDateFormat para el formato de fecha y hora
            SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // Parsear la cadena a un objeto Date
            Date fechaHoraConcatenada = formatoFechaHora.parse(fechaHoraConcatenadaStr);

            return fechaHoraConcatenada;
        } catch (Exception e) {
            e.printStackTrace();
            return fechaActual;
        }
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }

    public String getDivClass() {
        return divClass;
    }

    public void setDivClass(String divClass) {
        this.divClass = divClass;
    }

}
