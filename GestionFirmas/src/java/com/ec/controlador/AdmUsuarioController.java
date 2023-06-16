/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Usuario;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;

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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;

/**
 *
 * @author Darwin
 */
public class AdmUsuarioController {

    private Usuario usuario;
    ServicioUsuario servicioUsuario = new ServicioUsuario();
    UserCredential credential = new UserCredential();
    private List<Usuario> listaUsuario = new ArrayList<>();
    private String buscar = "";

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, JRException, IOException {
        Selectors.wireComponents(view, this, false);
        buscarUsuario();
    }

    public AdmUsuarioController() {

        Session sess = Sessions.getCurrent();
        credential = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());

    }

    private void buscarUsuario() {
        listaUsuario = servicioUsuario.finAll(buscar);

    }

    @Command
    @NotifyChange({"listaUsuario", "buscar"})
    public void buscarLike() {

        buscarUsuario();
    }

    @Command
    @NotifyChange({"listaUsuario", "buscar"})
    public void nuevoUsuario() {
        try {

            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/administrador/nuevo/usuario.zul", null, null);
            window.doModal();
            buscarLike();
        } catch (Exception e) {
            Clients.showNotification("Ocurrio un error " + e.getMessage(),
                        Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
        }
    }

    @Command
    @NotifyChange({"listaUsuario", "buscar"})
    public void modificarUsuario(@BindingParam("valor") Usuario valor) {
        try {
//            if (Messagebox.show("¿Desea modificar el registro, recuerde que debe crear las reteniones nuevamente?", "Atención", Messagebox.YES | Messagebox.NO, Messagebox.INFORMATION) == Messagebox.YES) {
            final HashMap<String, Usuario> map = new HashMap<String, Usuario>();

            map.put("valor", valor);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/administrador/nuevo/usuario.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            Clients.showNotification("Ocurrio un error " + e.getMessage(),
                        Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
        }
    }

    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }

    public List<Usuario> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

}
