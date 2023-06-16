/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import java.io.File;
import java.sql.SQLException;
import net.sf.jasperreports.engine.JRException;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.image.AImage;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class VisorImagenController {

    @Wire
    Window windowRecibidoContenedor;
    AImage fotoGeneral = null;

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") AImage recibido, @ContextParam(ContextType.VIEW) Component view) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, JRException {
        Selectors.wireComponents(view, this, false);

        if (recibido != null) {
            fotoGeneral = recibido;
        } else {
            Messagebox.show("No presenta una imagen registrada", "Atención", Messagebox.OK, Messagebox.ERROR);
        }

    }

    public AImage getFotoGeneral() {
        return fotoGeneral;
    }

    public void setFotoGeneral(AImage fotoGeneral) {
        this.fotoGeneral = fotoGeneral;
    }

    @Command
    public void cerrar() {
        windowRecibidoContenedor.detach();
    }
}
