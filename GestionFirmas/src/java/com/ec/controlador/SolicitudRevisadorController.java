/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.controlador.consumirws.RequestApiEmpresa;
import com.ec.controlador.consumirws.ServiciosRest;
import com.ec.entidad.Solicitud;
import com.ec.entidad.Usuario;
import com.ec.entidad.EstadoProceso;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.HelperPersistencia;
import com.ec.servicio.ServicioEstadoProceso;
import com.ec.servicio.ServicioSolicitud;

import com.ec.servicio.ServicioUsuario;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.util.media.AMedia;
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
public class SolicitudRevisadorController {

    private Usuario usuario;
    ServicioUsuario servicioUsuario = new ServicioUsuario();
    UserCredential credential = new UserCredential();
    ServicioSolicitud servicioSolicitud = new ServicioSolicitud();
    ServicioEstadoProceso servicioEstadoProceso = new ServicioEstadoProceso();
    private List<Solicitud> listaDatos = new ArrayList<Solicitud>();
    private List<EstadoProceso> listadoEstados = new ArrayList<EstadoProceso>();
    private EstadoProceso tipoSolSelected = null;
    private String buscar = "";
    private Date fechainicio = new Date();
    private Date fechafin = new Date();
    //subir pdf
    private String filePath;
    byte[] buffer = new byte[1024 * 1024];
    private AImage fotoGeneral = null;

    Connection con = null;
    //reporte
    AMedia fileContent = null;

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
        cargarDatos();
    }

    private void cargarDatos() {
        listadoEstados = servicioEstadoProceso.finAll();
    }

    public SolicitudRevisadorController() {

        Session sess = Sessions.getCurrent();
        credential = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        buscarSolicitudes();
    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void buscarLike() {

        listaDatos = servicioSolicitud.findLikeSolicitud(buscar, credential.getUsuarioSistema());
    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void eliminarPaciente(@BindingParam("valor") Solicitud valor) {
        try {
            if (Messagebox.show("¿Esta seguro de eliminar el paciente?", "Atención", Messagebox.YES | Messagebox.NO, Messagebox.INFORMATION) == Messagebox.YES) {

                servicioSolicitud.modificar(valor);
                buscarLike();
            }
        } catch (Exception e) {
            Clients.showNotification("Ocurrio un error " + e.getMessage(),
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
        }
    }

    @Command
    public void pdfSolicitudAll() {

        EntityManager emf = HelperPersistencia.getEMF();

        try {
            emf.getTransaction().begin();
            con = emf.unwrap(Connection.class);

            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                    .getRealPath("/reportes");
            String reportPath = "";

            reportPath = reportFile + File.separator + "listadosolicitudAll.jasper";

            Map<String, Object> parametros = new HashMap<String, Object>();

            //  parametros.put("codUsuario", String.valueOf(credentialLog.getAdUsuario().getCodigoUsuario()));
            parametros.put("busqueda", buscar);

            if (con != null) {
                System.out.println("Conexión Realizada Correctamenteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            }
            FileInputStream is = null;
            is = new FileInputStream(reportPath);

            byte[] buf = JasperRunManager.runReportToPdf(is, parametros, con);
            InputStream mediais = new ByteArrayInputStream(buf);
            AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", mediais);
            fileContent = amedia;
            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
//para pasar al visor
            map.put("pdf", fileContent);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/modal/visorreporte.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            System.out.println("ERROR EL PRESENTAR EL REPORTE " + e.getMessage());
        } finally {
            if (emf != null) {
                emf.getTransaction().commit();
            }

        }

    }

    @Command
    public void pdfSolicitudFecha() {

        EntityManager emf = HelperPersistencia.getEMF();

        try {
            emf.getTransaction().begin();
            con = emf.unwrap(Connection.class);

            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                    .getRealPath("/reportes");
            String reportPath = "";

            reportPath = reportFile + File.separator + "listadosolicitudGeneralFecha.jasper";

            Map<String, Object> parametros = new HashMap<String, Object>();

            //  parametros.put("codUsuario", String.valueOf(credentialLog.getAdUsuario().getCodigoUsuario()));
            parametros.put("busqueda", buscar);
            parametros.put("inicio", fechainicio);
            parametros.put("fin", fechafin);

            if (con != null) {
                System.out.println("Conexión Realizada Correctamenteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            }
            FileInputStream is = null;
            is = new FileInputStream(reportPath);

            byte[] buf = JasperRunManager.runReportToPdf(is, parametros, con);
            InputStream mediais = new ByteArrayInputStream(buf);
            AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", mediais);
            fileContent = amedia;
            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
//para pasar al visor
            map.put("pdf", fileContent);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/modal/visorreporte.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            System.out.println("ERROR EL PRESENTAR EL REPORTE " + e.getMessage());
        } finally {
            if (emf != null) {
                emf.getTransaction().commit();
            }

        }

    }

    @Command
    public void pdfSolicitudIndividual(@BindingParam("valor") Solicitud valor) {

        EntityManager emf = HelperPersistencia.getEMF();

        try {
            emf.getTransaction().begin();
            con = emf.unwrap(Connection.class);

            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                    .getRealPath("/reportes");
            String reportPath = "";

            reportPath = reportFile + File.separator + "solicitud.jasper";

            Map<String, Object> parametros = new HashMap<String, Object>();

            //  parametros.put("codUsuario", String.valueOf(credentialLog.getAdUsuario().getCodigoUsuario()));
            parametros.put("idSolicitud", valor.getIdSolicitud());

            if (con != null) {
                System.out.println("Conexión Realizada Correctamenteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            }
            FileInputStream is = null;
            is = new FileInputStream(reportPath);

            byte[] buf = JasperRunManager.runReportToPdf(is, parametros, con);
            InputStream mediais = new ByteArrayInputStream(buf);
            AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", mediais);
            fileContent = amedia;
            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
//para pasar al visor
            map.put("pdf", fileContent);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/modal/visorreporte.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            System.out.println("ERROR EL PRESENTAR EL REPORTE " + e.getMessage());
        } finally {
            if (emf != null) {
                emf.getTransaction().commit();
            }

        }

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

    @Command
    public void cambiarEstadoSol(@BindingParam("valor") Solicitud valor) throws JRException, IOException, NamingException, SQLException {
        try {
            final HashMap<String, Solicitud> map = new HashMap<String, Solicitud>();

            map.put("valor", valor);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/revisador/estadoSolicitud.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            Messagebox.show("Error " + e.toString(), "Atención", Messagebox.OK, Messagebox.INFORMATION);
        }
    }
    
    @Command
    @NotifyChange({"listaDetalleTipoFirmas", "tipoFirmaSelected"})
    public void consultaDetalleTipoFirma() {

        //listaDatos = servicioDetalleTipoFirma.findByTipoFirma(tipoFirmaSelected);

    }
    @Command
    public void generarFirma(@BindingParam("valor") Solicitud valor) {

        ServiciosRest rest= new ServiciosRest();
        RequestApiEmpresa param= new RequestApiEmpresa(valor.getIdSolicitud(), valor.getIdUsuario().getIdUsuario());
        rest.obtenerFirmaEmpresa(param,valor.getSolTipo());

    }

    private void buscarSolicitudes() {
        //listaDatos = servicioSolicitud.findLikeSolicitud(buscar, credential.getUsuarioSistema());
        
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

}
