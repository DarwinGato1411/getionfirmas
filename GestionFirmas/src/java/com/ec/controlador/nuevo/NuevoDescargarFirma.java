/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.nuevo;

import com.ec.entidad.EstadoFirma;
import com.ec.entidad.Solicitud;
import com.ec.entidad.Usuario;
import com.ec.servicio.ServicioSolicitud;
import com.ec.servicio.ServicioUsuario;
import com.ec.utilitario.MailerClass;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.activation.MimetypesFileTypeMap;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
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

    ServicioSolicitud servicioSolicitud = new ServicioSolicitud();

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Solicitud valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        this.entidad = valor;

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

        try {

            if (usuPassword.equals("") || usuPasswordVer.equals("")) {
                
                sweetAltert("error","Password", "Ingrese un password ") ;
               
                return;
            }
            if (usuPassword.length() < 8) {
                  sweetAltert("error","Password", "El password debe contener almenos 8 caractere entre letras y numeros") ;
                
                return;
            }
            if (usuPassword.equals(usuPasswordVer)) {
                EstadoFirma estadoFirma = new EstadoFirma();
                estadoFirma.setIdEstadoFirma(2);
                estadoFirma.setEstDescripcion("ENTREGADO");
                entidad.setIdEstadoFirma(estadoFirma);
                servicioSolicitud.modificar(entidad);
                try {

                    String directorioReportes = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/reportes");

                    String pathSalida = directorioReportes + File.separator + "alpha.p12";
                    System.out.println("path p12 " + pathSalida);
                    File dosfile = new File(pathSalida);
                    if (dosfile.exists()) {
                        FileInputStream inputStream = new FileInputStream(dosfile);
                        Filedownload.save(inputStream, new MimetypesFileTypeMap().getContentType(dosfile), dosfile.getName());
                    }

                    MailerClass mail = new MailerClass();
                    mail.sendMailSimple(entidad.getSolMail(), "Firma electronica descargada", entidad.getSolCedula(), entidad.getSolCedula(), entidad.getSolNombre() + " " + entidad.getSolApellido1());
                    wDescargar.detach();
                } catch (FileNotFoundException e) {
                    System.out.println("ERROR AL DESCARGAR EL ARCHIVO" + e.getMessage());
                }
                
                 sweetAltert("success","Firma electronica", "Firma descargada correctamente") ;
               
            } else {
                 sweetAltert("error","Password", "Los password ingresado no son iguales") ;
              
            }

        } catch (Exception e) {

            Clients.showNotification("Correo no enviado " + e.getMessage(),
                        Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
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
}
