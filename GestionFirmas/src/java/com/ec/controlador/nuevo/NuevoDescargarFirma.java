/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.nuevo;

import com.ec.controlador.consumirws.RequestApiEmpresa;
import com.ec.controlador.consumirws.RespuestaProceso;
import com.ec.controlador.consumirws.ServiciosRest;
import com.ec.entidad.EstadoFirma;
import com.ec.entidad.Solicitud;
import com.ec.entidad.Usuario;
import com.ec.servicio.ServicioSolicitud;
import com.ec.servicio.ServicioUsuario;
import com.ec.utilitario.MailerClass;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import javax.activation.MimetypesFileTypeMap;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class NuevoDescargarFirma {

    @Wire
    Window wDescargar;
    private Solicitud entidad = new Solicitud();
    ServicioUsuario servicio = new ServicioUsuario();
    private String usuPassword = "";
    private String usuPasswordVer = "";
    private boolean aceptarTerminos = false;

    //Ver pdf
    byte[] buffer = new byte[2 * 1024 * 1024];
    AMedia fileContent = null;

    ServicioSolicitud servicioSolicitud = new ServicioSolicitud();

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Solicitud valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        if (valor != null) {
            this.entidad = valor;
            aceptarTerminos = entidad.isSolAceptarTC();
            System.out.println(aceptarTerminos);
        }

    }

    public String getUsuPassword() {
        return usuPassword;
    }

    public void setUsuPassword(String usuPassword) {
        this.usuPassword = usuPassword;
    }

    public String getUsuPasswordVer() {
        return usuPasswordVer;
    }

    public void setUsuPasswordVer(String usuPasswordVer) {
        this.usuPasswordVer = usuPasswordVer;
    }

    @Command
    public void generarFirma() {
        //servicioSolicitud.modificar(entidad);
        try {

            if (usuPassword.equals("") || usuPasswordVer.equals("")) {

                sweetAltert("error", "Password", "Ingrese un password ");

                return;
            }
            if (usuPassword.length() < 8) {
                sweetAltert("error", "Password", "El password debe contener almenos 8 caractere entre letras y numeros");
                return;
            }
            if (usuPassword.equals(usuPasswordVer)) {
                EstadoFirma estadoFirma = new EstadoFirma();
                estadoFirma.setIdEstadoFirma(2);
                estadoFirma.setEstDescripcion("ENTREGADO");
                entidad.setIdEstadoFirma(estadoFirma);
                servicioSolicitud.modificar(entidad);
                try {

                    ServiciosRest ws = new ServiciosRest();
                    RequestApiEmpresa param = new RequestApiEmpresa(entidad.getIdSolicitud(), entidad.getIdUsuario().getIdUsuario());
                    param.setClave(usuPassword);
                    RespuestaProceso proceso = ws.obtenerFirmaEmpresa(param, entidad.getSolTipo());

                    String pathSalida = proceso.getObservacion();
                    System.out.println("path p12 " + pathSalida);
                    System.out.println(proceso.getCodigo());
                    System.out.println(proceso.getMensaje());
                    System.out.println(proceso.getObservacion());
                    if (pathSalida != null) {
                        File dosfile = new File(pathSalida);
                        if (dosfile.exists()) {
                            FileInputStream inputStream = new FileInputStream(dosfile);
                            Filedownload.save(inputStream, new MimetypesFileTypeMap().getContentType(dosfile), dosfile.getName());
                        }

                        MailerClass mail = new MailerClass();
                        mail.sendMailSimple(entidad.getSolMail(), "Firma electronica descargada", entidad.getSolCedula(), entidad.getSolCedula(), entidad.getSolNombre() + " " + entidad.getSolApellido1());
                        sweetAltert("success", "Firma electronica", "Firma descargada correctamente");
                    } else {
                        sweetAltert("warning", "Firma electronica", "Firma no descargada, no se encontró el path de salida");
                    }

                    wDescargar.detach();
                } catch (FileNotFoundException e) {
                    System.out.println("ERROR AL DESCARGAR EL ARCHIVO" + e.getMessage());
                }

            } else {
                sweetAltert("error", "Password", "Los password ingresado no son iguales");

            }

        } catch (RemoteException e) {

            Clients.showNotification("Error al generar la firma " + e.getMessage(),
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
        }

    }

    @Command
    @NotifyChange({"aceptarTerminos"})
    public void aceptarTerminosCondiciones() {

        System.out.println(aceptarTerminos);
        entidad.setSolAceptarTC(aceptarTerminos);
    }

    @Command
    public void verTerminosCondiciones() {
        try {

            String nombreArchivo = "TerminosCondiciones.pdf";

            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                    .getRealPath("/reportes");
            String rutaArchivo = reportFile + File.separator + nombreArchivo;
            System.out.println(rutaArchivo);
            File f = new File(rutaArchivo);
            buffer = new byte[2 * 1024 * 1024];
// web\reportes\TerminosCondiciones.pdf
            FileInputStream fs = new FileInputStream(f);
            fs.read(buffer);
            fs.close();
            ByteArrayInputStream is = new ByteArrayInputStream(buffer);
            AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", is);
            fileContent = amedia;
            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
//para pasar al visor
            map.put("pdf", fileContent);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/visor/visorreporte.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            sweetAltert("error", "Error visualización", "No existe el archivo " + e.getMessage());
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

    public boolean isAceptarTerminos() {
        return aceptarTerminos;
    }

    public void setAceptarTerminos(boolean aceptarTerminos) {
        this.aceptarTerminos = aceptarTerminos;
    }

}
