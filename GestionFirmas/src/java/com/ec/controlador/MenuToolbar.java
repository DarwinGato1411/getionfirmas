/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;


import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;

/**
 *
 * @author gato
 */
public class MenuToolbar  {

    public MenuToolbar() {

    }

  

  

    @Command
    public void nuevoCliente() {

        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/cliente.zul", null, null);
        window.doModal();

    }
    @Command
    public void nuevoProducto() {

        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/producto.zul", null, null);
        window.doModal();

    }
}
