/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.nuevo;

import com.ec.entidad.Usuario;
import com.ec.servicio.ServicioUsuario;

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
public class NuevoUsuario {

    @Wire
    Window wUsuario;
    private Usuario entidad = new Usuario();
    ServicioUsuario servicio = new ServicioUsuario();
    private String accion = "create";
    private String usuNivel = "1";

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Usuario valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        if (valor != null) {
            this.entidad = valor;
            usuNivel = this.entidad.getUsuNivel().toString();
            accion = "update";

        } else {
            this.entidad = new Usuario();
            accion = "create";

        }

    }

    @Command
    public void guardar() {

        /*validar que no exista dos usuarios iguales*/
        if (entidad.getUsuRuc() != null
                    && entidad.getUsuLogin() != null
                    && entidad.getUsuPassword() != null) {
            entidad.setUsuNivel(Integer.valueOf(usuNivel));
            if (Integer.valueOf(usuNivel) == 1) {
                entidad.setUsuTipoUsuario("DISTRIBUIDOR");
            } else if (Integer.valueOf(usuNivel) == 2) {
                entidad.setUsuTipoUsuario("ADMINISTRADOR");
            } else if (Integer.valueOf(usuNivel) == 3) {
                entidad.setUsuTipoUsuario("REVISADOR");
            } else if (Integer.valueOf(usuNivel) == 4) {
                entidad.setUsuTipoUsuario("CLIENTE");
            }
            if (accion.equals("create")) {
                servicio.crear(entidad);
                wUsuario.detach();
            } else {
                servicio.modificar(entidad);
                wUsuario.detach();
            }
            Clients.showNotification("Registro correcto",
                        Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 2000, true);
        } else {
            Clients.showNotification("Verifique la información ingresada",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
        }
    }

    public Usuario getEntidad() {
        return entidad;
    }

    public void setEntidad(Usuario entidad) {
        this.entidad = entidad;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getUsuNivel() {
        return usuNivel;
    }

    public void setUsuNivel(String usuNivel) {
        this.usuNivel = usuNivel;
    }

}
