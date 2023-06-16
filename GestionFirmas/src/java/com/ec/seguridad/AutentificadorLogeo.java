/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.seguridad;

import com.ec.entidad.Usuario;
import com.ec.servicio.ServicioUsuario;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.image.AImage;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

public class AutentificadorLogeo implements AutentificadorService, Serializable {

    private static final long serialVersionUID = 1L;
    ServicioUsuario servicioUsuario = new ServicioUsuario();

    private String filePath;
    byte[] buffer = new byte[1024 * 1024];
    private AImage fotoGeneral = null;

    public UserCredential getUserCredential() {
        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        if (cre == null) {
            cre = new UserCredential();
            sess.setAttribute(EnumSesion.userCredential.getNombre(), cre);
        }
        return cre;
    }


    /*
     * Cambiar el método en el ModeloUsuario para traer datos de los usuarios de hibernate
     */
    public boolean login(String nombreUsuario, String claveUsuario) {
        Usuario dato = (Usuario) servicioUsuario.findUsuarioPorNombre(nombreUsuario);
        if (dato != null) {
            if (dato.getUsuFoto() != null) {
                try {
                    System.out.println("PATH" + dato.getUsuFoto());
                    fotoGeneral = new AImage("fotoPedido", Imagen_A_Bytes(dato.getUsuFoto()));
//                Imagen_A_Bytes(empresa.getIdUsuario().getUsuFoto());
                } catch (FileNotFoundException e) {
                    System.out.println("error imagen " + e.getMessage());
                } catch (IOException ex) {
                    Logger.getLogger(AutentificadorLogeo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
//        System.out.println("usuario " + nombreUsuario);
//        System.out.println("Valor recuperado");
//        System.out.println("-------------->"+dato.getUsuLogin());
        if (dato == null) {
            return false;
        }
        if (!dato.getUsuLogin().equals(nombreUsuario) || !dato.getUsuPassword().equals(claveUsuario)) {
            return false;
        }

        Session sess = Sessions.getCurrent();
        UserCredential cre = new UserCredential(dato, dato.getUsuLogin(), dato.getUsuPassword(), dato.getUsuNivel(), dato.getUsuNombre(), fotoGeneral,dato.getTipoUsuarioText());
        // System.out.println("VALOR DE LA CREDENCIAL ASIGNADA A LA SESSION"+EnumSesion.userCredential.getNombre());

        sess.setAttribute(EnumSesion.userCredential.getNombre(), cre);

        return true;
    }

    public byte[] Imagen_A_Bytes(String pathImagen) throws FileNotFoundException {
        String reportPath = "";
        reportPath = pathImagen;
        File file = new File(reportPath);

        FileInputStream fis = new FileInputStream(file);
        //create FileInputStream which obtains input bytes from a file in a file system
        //FileInputStream is meant for reading streams of raw bytes such as image data. For reading streams of characters, consider using FileReader.

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                //Writes to this byte array output stream
                bos.write(buf, 0, readNum);
//                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
        }

        byte[] bytes = bos.toByteArray();
        return bytes;
    }

    public void logout() {
        Session sess = Sessions.getCurrent();
        sess.removeAttribute(EnumSesion.userCredential.getNombre());
    }
}
