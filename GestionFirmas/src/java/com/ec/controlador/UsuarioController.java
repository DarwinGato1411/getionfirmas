/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Usuario;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioGeneral;
import com.ec.servicio.ServicioUsuario;
import com.ec.utilitario.ArchivoUtils;
import com.ec.utilitario.InfoPersona;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import javax.xml.xpath.XPathExpressionException;
import net.sf.jasperreports.engine.JRException;
import org.json.JSONException;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

/**
 *
 * @author Darwin
 */
public class UsuarioController {

    @Wire
    Window wUsuario;
    private Usuario usuario;
    ServicioUsuario servicioUsuario = new ServicioUsuario();

    UserCredential credential = new UserCredential();

    ServicioGeneral servicioGeneral = new ServicioGeneral();

    private String accion = "create";

    public UsuarioController() {
        Session sess = Sessions.getCurrent();
        credential = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
//        usuario = credential.getUsuarioSistema();

    }

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Usuario valor, @ContextParam(ContextType.VIEW) Component view) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, JRException, IOException {
        Selectors.wireComponents(view, this, false);
        if (valor != null) {
            this.usuario = valor;
            accion = "update";
        } else {
            usuario = new Usuario();
            usuario.setUsuNivel(1);
            accion = "create";
        }
    }

    @Command
    @NotifyChange({"usuario"})
    public void buscarInformacion() throws URISyntaxException, IOException, XPathExpressionException, JSONException {
        InfoPersona aduana = new InfoPersona();
        String nombre = "";
        if (usuario.getUsuRuc() != null) {
            if (!usuario.getUsuRuc().equals("")) {
                String cedulaBuscar = "";
                if (usuario.getUsuRuc().length() == 13) {
                    cedulaBuscar = usuario.getUsuRuc();
                    nombre = ArchivoUtils.obtenerPorRuc(cedulaBuscar);
                    usuario.setUsuNombre(nombre);

                } else if (usuario.getUsuRuc().length() == 10) {
                    cedulaBuscar = usuario.getUsuRuc();
                    aduana = ArchivoUtils.obtenerPorCedula(cedulaBuscar);
                    if (!aduana.getNombre().equals("")) {
                        usuario.setUsuNombre(aduana.getNombre());
                        usuario.setUsuCiudad(aduana.getDireccion().split("/")[0]);

                    } else {
                        nombre = ArchivoUtils.obtenerPorRuc(cedulaBuscar + "001");
                        usuario.setUsuNombre(nombre);
                    }
                }

            }
        }

    }

    @Command
    public void guardar() {

        if (accion.equals("create")) {
            if (servicioUsuario.findUsuarioPorNombre(usuario.getUsuLogin()) != null) {
                Clients.showNotification("ya existe una cuenta con ese usuario ... ",
                            Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
                return;
            }
            servicioUsuario.crear(usuario);
            servicioGeneral.generarHorario(usuario.getIdUsuario());
            Clients.showNotification("Guardado correctamente... ",
                        Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 3000, true);
            wUsuario.detach();

        } else {
            servicioUsuario.modificar(usuario);
            Clients.showNotification("Guardado correctamente... ",
                        Clients.NOTIFICATION_TYPE_INFO, null, "end_center", 3000, true);
            wUsuario.detach();
        }

    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
