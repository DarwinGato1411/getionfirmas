/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.seguridad;

import com.ec.entidad.Usuario;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Executions;

public class UserCredential implements Serializable {

    private static final long serialVersionUID = 1L;
    String account;
    String name;
    String nombreUsuario;

    private Integer nivelUsuario;
    private Usuario usuarioSistema;
    private Boolean puedeEditar = Boolean.FALSE;
    Set<String> roles = new HashSet<String>();
    /*foto usuario*/
    
     String perfil;

    private String filePath;
    byte[] buffer = new byte[1024 * 1024];
    private AImage fotoGeneral = null;

    public UserCredential(Usuario usuario, String account, String name, Integer nivelUsuario, String nombreUsuario) {
        this.usuarioSistema = usuario;
        this.name = name;
        this.account = account;
        this.nivelUsuario = nivelUsuario;
        this.nombreUsuario = nombreUsuario;
    }

    public UserCredential(Usuario usuario, String account, String name, Integer nivelUsuario, String nombreUsuario, AImage fotoGeneral) {
        this.usuarioSistema = usuario;
        this.name = name;
        this.account = account;
        this.nivelUsuario = nivelUsuario;
        this.nombreUsuario = nombreUsuario;
        this.fotoGeneral = fotoGeneral;
    }
    public UserCredential(Usuario usuario, String account, String name, Integer nivelUsuario, String nombreUsuario, AImage fotoGeneral,String perfil) {
        this.usuarioSistema = usuario;
        this.name = name;
        this.account = account;
        this.nivelUsuario = nivelUsuario;
        this.nombreUsuario = nombreUsuario;
        this.fotoGeneral = fotoGeneral;
        this.perfil=perfil;
    }

    public UserCredential() {
        this.account = "anonymous";
        this.name = "Anonymous";
        roles.add("anonymous");
    }

    public boolean isAnonymous() {
        return hasRole("anonymous") || "anonymous".equals(account);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    public void addRole(String role) {
        roles.add(role);
    }

    public Integer getNivelUsuario() {
        return nivelUsuario;
    }

    public void setNivelUsuario(Integer nivelUsuario) {
        this.nivelUsuario = nivelUsuario;
    }

    public Usuario getUsuarioSistema() {
        return usuarioSistema;
    }

    public void setUsuarioSistema(Usuario usuarioSistema) {
        this.usuarioSistema = usuarioSistema;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Boolean getPuedeEditar() {
        if (usuarioSistema.getUsuNivel() == 1 || usuarioSistema.getUsuNivel() == 2) {
            puedeEditar = Boolean.TRUE;
        } else {
            puedeEditar = Boolean.FALSE;
        }

        return puedeEditar;
    }

    public void setPuedeEditar(Boolean puedeEditar) {
        this.puedeEditar = puedeEditar;
    }

    public AImage getFotoGeneral() throws FileNotFoundException, IOException {
        if (usuarioSistema.getUsuFoto() != null) {
            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                        .getRealPath("/iconos");
            try {
                System.out.println("PATH" + usuarioSistema.getUsuFoto());
                fotoGeneral = new AImage("fotoPedido", Imagen_A_Bytes(usuarioSistema.getUsuFoto()));
//                Imagen_A_Bytes(empresa.getIdUsuario().getUsuFoto());
            } catch (FileNotFoundException e) {
                System.out.println("error imagen " + e.getMessage());

                fotoGeneral = new AImage("fotoPedido", Imagen_A_Bytes(reportFile + File.separator + "user.png"));
            } catch (IOException ex) {
                fotoGeneral = new AImage("fotoPedido", Imagen_A_Bytes(reportFile + File.separator + "user.png"));
//                Logger.getLogger(AutentificadorLogeo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return fotoGeneral;
    }

    public void setFotoGeneral(AImage fotoGeneral) {
        this.fotoGeneral = fotoGeneral;
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

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
    
    
}
