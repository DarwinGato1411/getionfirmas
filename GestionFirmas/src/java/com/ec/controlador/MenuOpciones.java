/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

/**
 *
 * @author gato
 */
public class MenuOpciones extends SelectorComposer<Component> {

    UserCredential credential = new UserCredential();
    private String acceso = "";

    public MenuOpciones() {
        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        credential = cre;

    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

    }

    @Listen("onClick = #btnSolicitudRevisador")
    public void btnSolicitudRevisador() {
        Executions.sendRedirect("/revisador/solicitud.zul");
    }

    @Listen("onClick = #btnSolicitudRevisadorEstadoSol")
    public void btnSolicitudRevisadorEstadoSol() {
        Executions.sendRedirect("/revisador/solicitudEstSol.zul");
    }
    
     @Listen("onClick = #btnSolicitudRevisadorEstadoFir")
    public void btnSolicitudRevisadorEstadoFir() {
        Executions.sendRedirect("/revisador/solicitudEstFirma.zul");
    }
     @Listen("onClick = #btnSusFirmas")
    public void btnSusFirmas() {
        Executions.sendRedirect("/cliente/solicitud.zul");
    }
}
