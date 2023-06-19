/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.nuevo;

import com.ec.entidad.Ciudad;
import com.ec.entidad.Nacionalidad;
import com.ec.entidad.Parametrizar;
import com.ec.entidad.Provincia;
import com.ec.entidad.Solicitud;
import com.ec.entidad.Usuario;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioCuidad;
import com.ec.servicio.ServicioEstadoProceso;
import com.ec.servicio.ServicioNacionalidad;
import com.ec.servicio.ServicioParametrizar;
import com.ec.servicio.ServicioProvincia;
import com.ec.servicio.ServicioSolicitud;
import com.ec.utilitario.ArchivoUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class NuevaSolicitud {

    @Wire
    Window wSolicitud;

    private Solicitud entidad = new Solicitud();
    ServicioSolicitud servicio = new ServicioSolicitud();
    private String accion = "create";
    private String usuNivel = "1";
    private String tipoSolicitud = "PN";

    ServicioParametrizar servicioParametrizar = new ServicioParametrizar();
    private Parametrizar parametrizar = new Parametrizar();
    private Usuario usuario = new Usuario();
    //subir fotografia cedula
    private String filePath;
    byte[] buffer = new byte[1024 * 1024];
    private AImage fotoGeneral = null;
    private AImage fotoReverso = null;
    private AImage fotoSelfi = null;
    private AMedia pdfRuc = null;

    ServicioNacionalidad servicioNacionalidad = new ServicioNacionalidad();
    private List<Nacionalidad> listaNacionalidad = new ArrayList<>();

    ServicioProvincia servicioProvincia = new ServicioProvincia();
    private List<Provincia> listaProvincias = new ArrayList<>();
    private Provincia provinciaSelected = null;
    ServicioCuidad servicioCiudad = new ServicioCuidad();
    private List<Ciudad> listaCiudades = new ArrayList<Ciudad>();

    ServicioEstadoProceso servicioEstadoProceso = new ServicioEstadoProceso();

    @Wire
    private Div rle;
    @Wire
    private Div me;
    @Wire
    private Div contenedorMR;

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Solicitud valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);

        cargarDatos();
        if (valor != null) {
            this.entidad = valor;
            accion = "update";
            provinciaSelected = entidad.getIdCiudad() != null ? entidad.getIdCiudad().getIdProvincia() : null;
            if (provinciaSelected != null) {
                listaCiudades = servicioCiudad.findByProvincia(provinciaSelected);
            }

            cargarVistaTiposSol(valor.getSolTipo());
        } else {

            //muestra 7 dias atras
            Calendar calendar = Calendar.getInstance(); //obtiene la fecha de hoy 
            calendar.add(Calendar.YEAR, -18); //el -3 indica que se le restaran 3 dias 

            this.entidad = new Solicitud();
            this.entidad.setSolFechaNacimiento(calendar.getTime());
            accion = "create";

        }

        try {
            if (entidad.getSolPathRuc() != null) {
                try {
                    System.out.println("PDF RUC" + entidad.getSolPathSelfi());
                    pdfRuc = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(entidad.getSolPathRuc()));

                } catch (FileNotFoundException e) {
                    System.out.println("error imagen " + e.getMessage());
                }
            }

            if (entidad.getSolPathCedulaAnverso() != null) {
                try {
                    System.out.println("FOTOGRAFIA ANVERSO" + entidad.getSolPathCedulaAnverso());
                    fotoGeneral = new AImage("fotografia", Imagen_A_Bytes(entidad.getSolPathCedulaAnverso()));
//                Imagen_A_Bytes(empresa.getIdUsuario().getUsuFoto());
                } catch (FileNotFoundException e) {
                    System.out.println("error imagen " + e.getMessage());
                }
            }
            if (entidad.getSolPathCedulaReverso() != null) {
                try {
                    System.out.println("FOTOGRAFIA getSolPathCedulaReverso" + entidad.getSolPathCedulaReverso());
                    fotoReverso = new AImage("fotografia", Imagen_A_Bytes(entidad.getSolPathCedulaReverso()));
//                Imagen_A_Bytes(empresa.getIdUsuario().getUsuFoto());
                } catch (FileNotFoundException e) {
                    System.out.println("error imagen " + e.getMessage());
                }
            }
            if (entidad.getSolPathSelfi() != null) {
                try {
                    System.out.println("FOTOGRAFIA getSolPathSelfi" + entidad.getSolPathSelfi());
                    fotoSelfi = new AImage("fotografia", Imagen_A_Bytes(entidad.getSolPathSelfi()));
//                Imagen_A_Bytes(empresa.getIdUsuario().getUsuFoto());
                } catch (FileNotFoundException e) {
                    System.out.println("error imagen " + e.getMessage());
                }
            }

        } catch (IOException e) {
        }

    }

    private void cargarDatos() {
        listaNacionalidad = servicioNacionalidad.finAll();
        listaProvincias = servicioProvincia.finAll();
    }

    public NuevaSolicitud() {

        Session sess = Sessions.getCurrent();
        UserCredential credential = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        usuario = credential.getUsuarioSistema();
        parametrizar = servicioParametrizar.findActivo();
    }

    @Command
    @NotifyChange({"listaCiudades", "provinciaSelected"})
    public void consultarCiudad() {
        listaCiudades = servicioCiudad.findByProvincia(provinciaSelected);

    }

    @Command
    @NotifyChange("entidad")
    public void obtenerEdad() {
        if (entidad.getSolFechaNacimiento() != null) {
            BigDecimal edad = ArchivoUtils.obtenerEdad(entidad.getSolFechaNacimiento());
            entidad.setSolEdad(edad.intValue());
        }
    }

    @Command
    public void guardar() {
        if (entidad.getSolNombre() != null
                && entidad.getSolRuc() != null
                && entidad.getSolCelular() != null) {
//            entidad.setUsuNivel(Integer.valueOf(usuNivel));
            if (accion.equals("create")) {
                entidad.setIdEstadoProceso(servicioEstadoProceso.findBySigla("ING"));
                entidad.setIdUsuario(usuario);
                servicio.crear(entidad);
                wSolicitud.detach();
            } else {
                if (entidad.getIdEstadoProceso() == null) {
                    entidad.setIdEstadoProceso(servicioEstadoProceso.findBySigla("ING"));
                }

                servicio.modificar(entidad);
                wSolicitud.detach();
            }
            Clients.showNotification("Registro correcto",
                    Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 2000, true);
        } else {
            Clients.showNotification("Verifique la información ingresada",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
        }
    }

    public Solicitud getEntidad() {
        return entidad;
    }

    public void setEntidad(Solicitud entidad) {
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

    @Command
    @NotifyChange({"fileContent", "entidad", "fotoGeneral"})
    public void subirFotografiaCedula() throws InterruptedException, IOException {

        org.zkoss.util.media.Media media = Fileupload.get();
        if (media instanceof org.zkoss.image.Image) {
            String nombre = media.getName();

            if (media.getByteData().length > 5 * 1024 * 1024) {
                Messagebox.show("El arhivo seleccionado sobrepasa el tamaño de 5Mb.\n Por favor seleccione un archivo más pequeño.", "Atención", Messagebox.OK, Messagebox.ERROR);

                return;
            }
            filePath = parametrizar.getParBase() + File.separator + parametrizar.getParImagenes() + File.separator + "FOTO";

            File baseDir = new File(filePath);
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            Files.copy(new File(filePath + File.separator + media.getName()),
                    media.getStreamData());

            entidad.setSolPathCedulaAnverso(filePath + File.separator + media.getName());
            System.out.println("PATH SUBIR " + filePath + File.separator + media.getName());
            fotoGeneral = new AImage("fotografia", Imagen_A_Bytes(filePath + File.separator + media.getName()));

        } else {
            Clients.showNotification("El archivo seleccionado no es un imagen",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
        }
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

    public AImage getFotoGeneral() {
        return fotoGeneral;
    }

    public void setFotoGeneral(AImage fotoGeneral) {
        this.fotoGeneral = fotoGeneral;
    }

    @Command
    @NotifyChange({"fileContent", "entidad", "fotoReverso"})
    public void subirFotografiaCedulaReverso() throws InterruptedException, IOException {

        org.zkoss.util.media.Media media = Fileupload.get();
        if (media instanceof org.zkoss.image.Image) {
            String nombre = media.getName();

            if (media.getByteData().length > 10 * 1024 * 1024) {
                Messagebox.show("El arhivo seleccionado sobrepasa el tamaño de 10Mb.\n Por favor seleccione un archivo más pequeño.", "Atención", Messagebox.OK, Messagebox.ERROR);

                return;
            }
            filePath = parametrizar.getParBase() + File.separator + parametrizar.getParImagenes() + File.separator + "FOTO";

            File baseDir = new File(filePath);
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            Files.copy(new File(filePath + File.separator + media.getName()),
                    media.getStreamData());

            entidad.setSolPathCedulaReverso(filePath + File.separator + media.getName());
            System.out.println("PATH SUBIR " + filePath + File.separator + media.getName());
            fotoReverso = new AImage("fotografia", Imagen_A_Bytes(filePath + File.separator + media.getName()));

        }
    }

    @Command
    @NotifyChange({"fileContent", "entidad", "fotoSelfi"})
    public void subirSelfi() throws InterruptedException, IOException {

        org.zkoss.util.media.Media media = Fileupload.get();
        if (media instanceof org.zkoss.image.Image) {
            String nombre = media.getName();

            if (media.getByteData().length > 10 * 1024 * 1024) {
                Messagebox.show("El arhivo seleccionado sobrepasa el tamaño de 10Mb.\n Por favor seleccione un archivo más pequeño.", "Atención", Messagebox.OK, Messagebox.ERROR);

                return;
            }
            filePath = parametrizar.getParBase() + File.separator + parametrizar.getParImagenes() + File.separator + "FOTO";

            File baseDir = new File(filePath);
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            Files.copy(new File(filePath + File.separator + media.getName()),
                    media.getStreamData());

            entidad.setSolPathSelfi(filePath + File.separator + media.getName());
            System.out.println("PATH SUBIR " + filePath + File.separator + media.getName());
            fotoSelfi = new AImage("fotografia", Imagen_A_Bytes(filePath + File.separator + media.getName()));

        }
    }

    @Command
    public void personaNatural() {
        cargarVistaTiposSol("PN");
    }

    @Command
    public void repreLegalEmpresa() {
        cargarVistaTiposSol("RLE");
    }

    @Command
    public void miembEmpresa() {
        cargarVistaTiposSol("ME");
    }

    public void cargarVistaTiposSol(String tipoSol) {
        if (tipoSol.equals("PN")) {
            contenedorMR.setVisible(false);
        } else if (tipoSol.equals("RLE") || tipoSol.equals("ME")) {
            contenedorMR.setVisible(true);
            if (tipoSol.equals("RLE")) {
                rle.setVisible(true);
                me.setVisible(false);

            } else {
                rle.setVisible(false);
                me.setVisible(true);
            }
        }
    }

    public AImage getFotoReverso() {
        return fotoReverso;
    }

    public void setFotoReverso(AImage fotoReverso) {
        this.fotoReverso = fotoReverso;
    }

    public AImage getFotoSelfi() {
        return fotoSelfi;
    }

    public void setFotoSelfi(AImage fotoSelfi) {
        this.fotoSelfi = fotoSelfi;
    }

    public List<Nacionalidad> getListaNacionalidad() {
        return listaNacionalidad;
    }

    public void setListaNacionalidad(List<Nacionalidad> listaNacionalidad) {
        this.listaNacionalidad = listaNacionalidad;
    }

    public List<Provincia> getListaProvincias() {
        return listaProvincias;
    }

    public void setListaProvincias(List<Provincia> listaProvincias) {
        this.listaProvincias = listaProvincias;
    }

    public Provincia getProvinciaSelected() {
        return provinciaSelected;
    }

    public void setProvinciaSelected(Provincia provinciaSelected) {
        this.provinciaSelected = provinciaSelected;
    }

    public List<Ciudad> getListaCiudades() {
        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudad> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }
    /**
     * SUBIR PDF
     */
    @Command
    @NotifyChange({"pdfRuc", "entidad"})
    public void subirPDFRuc() throws InterruptedException, IOException {

        org.zkoss.util.media.Media media = Fileupload.get();
        if (media instanceof org.zkoss.util.media.AMedia) {
            String nombre = media.getName();

            if (!nombre.contains(".pdf")) {
                Clients.showNotification("Debe cargar un archivo PDF",
                            Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);

                return;
            }
            if (media.getByteData().length > 10 * 1024 * 1024) {
                Messagebox.show("El arhivo seleccionado sobrepasa el tamaño de 10Mb.\n Por favor seleccione un archivo más pequeño.", "Atención", Messagebox.OK, Messagebox.ERROR);

                return;
            }
            filePath = parametrizar.getParBase() + File.separator + parametrizar.getParImagenes() + File.separator + "FOTO";

            File baseDir = new File(filePath);
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            Files.copy(new File(filePath + File.separator + media.getName()),
                        media.getStreamData());

            entidad.setSolPathRuc(filePath + File.separator + media.getName());
            System.out.println("PATH SUBIR " + filePath + File.separator + media.getName());
            pdfRuc = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(filePath + File.separator + media.getName()));

        }
    }

    public AMedia getPdfRuc() {
        return pdfRuc;
    }

    public void setPdfRuc(AMedia pdfRuc) {
        this.pdfRuc = pdfRuc;
    }

}
