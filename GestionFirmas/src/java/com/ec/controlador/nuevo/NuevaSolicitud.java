/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.nuevo;

import com.ec.entidad.Ciudad;
import com.ec.entidad.DetalleTipoFirma;
import com.ec.entidad.Nacionalidad;
import com.ec.entidad.Parametrizar;
import com.ec.entidad.Provincia;
import com.ec.entidad.Solicitud;
import com.ec.entidad.TipoFirma;
import com.ec.entidad.Usuario;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioCuidad;
import com.ec.servicio.ServicioDetalleTipoFirma;
import com.ec.servicio.ServicioEstadoProceso;
import com.ec.servicio.ServicioNacionalidad;
import com.ec.servicio.ServicioParametrizar;
import com.ec.servicio.ServicioProvincia;
import com.ec.servicio.ServicioSolicitud;
import com.ec.servicio.ServicioTipoFirma;
import com.ec.utilitario.ArchivoUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
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
    private boolean mienbroEmpresa = false;
    private boolean repremiembros = false;
    private boolean cargoRepresentate = false;

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
    private AMedia pdfConstCompa = null;
    private AMedia pdfNombraRepre = null;
    private AMedia pdfAcecptacionNomb = null;
    private AMedia pdfRucEmpresa = null;
    private AMedia pdfCedRepreEmpresa = null;
    private AMedia pdfAutoriRepre = null;

    ServicioNacionalidad servicioNacionalidad = new ServicioNacionalidad();
    private List<Nacionalidad> listaNacionalidad = new ArrayList<>();

    ServicioProvincia servicioProvincia = new ServicioProvincia();
    private List<Provincia> listaProvincias = new ArrayList<>();
    private Provincia provinciaSelected = null;
    ServicioCuidad servicioCiudad = new ServicioCuidad();
    private List<Ciudad> listaCiudades = new ArrayList<Ciudad>();

    ServicioTipoFirma servicioTipoFirma = new ServicioTipoFirma();
    private List<TipoFirma> listaTipoFirmas = new ArrayList<TipoFirma>();
    private TipoFirma tipoFirmaSelected = null;

    ServicioDetalleTipoFirma servicioDetalleTipoFirma = new ServicioDetalleTipoFirma();
    private List<DetalleTipoFirma> listaDetalleTipoFirmas = new ArrayList<DetalleTipoFirma>();

    ServicioEstadoProceso servicioEstadoProceso = new ServicioEstadoProceso();

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
            tipoFirmaSelected = entidad.getIdDetalleTipoFirma()!= null ? entidad.getIdDetalleTipoFirma().getIdTipoFirma() : null;
            if (tipoFirmaSelected != null) {
                listaDetalleTipoFirmas = servicioDetalleTipoFirma.findByTipoFirma(tipoFirmaSelected);
            }

            cargarVistaTiposSol(valor.getSolTipo()==null?"":valor.getSolTipo());
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
            if (entidad.getSolPathConstitucionCompania() != null) {
                try {
                    System.out.println("PDF constitucion compania" + entidad.getSolPathConstitucionCompania());
                    pdfConstCompa = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(entidad.getSolPathConstitucionCompania()));

                } catch (FileNotFoundException e) {
                    System.out.println("error imagen " + e.getMessage());
                }
            }
            if (entidad.getSolPathNombramientoRepresentante() != null) {
                try {
                    System.out.println("PDF constitucion compania" + entidad.getSolPathNombramientoRepresentante());
                    pdfNombraRepre = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(entidad.getSolPathNombramientoRepresentante()));

                } catch (FileNotFoundException e) {
                    System.out.println("error imagen " + e.getMessage());
                }
            }

            if (entidad.getSolPathAceptacionNombramiento() != null) {
                try {
                    System.out.println("PDF constitucion compania" + entidad.getSolPathAceptacionNombramiento());
                    pdfAcecptacionNomb = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(entidad.getSolPathAceptacionNombramiento()));

                } catch (FileNotFoundException e) {
                    System.out.println("error imagen " + e.getMessage());
                }
            }

            if (entidad.getSolPathRucEmpresa() != null) {
                try {
                    System.out.println("PDF constitucion compania" + entidad.getSolPathRucEmpresa());
                    pdfRucEmpresa = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(entidad.getSolPathRucEmpresa()));

                } catch (FileNotFoundException e) {
                    System.out.println("error imagen " + e.getMessage());
                }
            }

            if (entidad.getSolPathCedulaRepresentanteEmpresa() != null) {
                try {
                    System.out.println("PDF constitucion compania" + entidad.getSolPathCedulaRepresentanteEmpresa());
                    pdfCedRepreEmpresa = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(entidad.getSolPathCedulaRepresentanteEmpresa()));

                } catch (FileNotFoundException e) {
                    System.out.println("error imagen " + e.getMessage());
                }
            }

            if (entidad.getSolPathAutorizacionRepresentante() != null) {
                try {
                    System.out.println("PDF constitucion compania" + entidad.getSolPathAutorizacionRepresentante());
                    pdfAutoriRepre = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(entidad.getSolPathAutorizacionRepresentante()));

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
        listaTipoFirmas = servicioTipoFirma.finAll();
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
    @NotifyChange({"listaDetalleTipoFirmas", "tipoFirmaSelected"})
    public void consultaDetalleTipoFirma() {

        listaDetalleTipoFirmas = servicioDetalleTipoFirma.findByTipoFirma(tipoFirmaSelected);

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
                entidad.setSolFechaCreacion(new Date());
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
    @NotifyChange({"mienbroEmpresa", "repremiembros", "cargoRepresentate"})
    public void personaNatural() {
        cargarVistaTiposSol("PN");
    }

    @Command
    @NotifyChange({"mienbroEmpresa", "repremiembros", "cargoRepresentate"})
    public void repreLegalEmpresa() {
        cargarVistaTiposSol("RLE");
    }

    @Command
    @NotifyChange({"mienbroEmpresa", "repremiembros", "cargoRepresentate"})
    public void miembEmpresa() {
        cargarVistaTiposSol("ME");
    }

    public void cargarVistaTiposSol(String tipoSol) {
        if (tipoSol.equals("PN")) {
            mienbroEmpresa = false;
            repremiembros = false;
            cargoRepresentate = true;
        } else if (tipoSol.equals("RLE") || tipoSol.equals("ME")) {
            if (tipoSol.equals("RLE")) {
                mienbroEmpresa = false;
                repremiembros = true;
                cargoRepresentate = true;
            } else if (tipoSol.equals("ME")) {
                repremiembros = true;
                mienbroEmpresa = true;
                cargoRepresentate = false;
            }
        }
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

    @Command
    @NotifyChange({"pdfConstCompa", "entidad"})
    public void subirConstCompa() throws InterruptedException, IOException {

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

            entidad.setSolPathConstitucionCompania(filePath + File.separator + media.getName());
            System.out.println("PATH SUBIR " + filePath + File.separator + media.getName());
            pdfConstCompa = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(filePath + File.separator + media.getName()));

        }
    }

    @Command
    @NotifyChange({"pdfNombraRepre", "entidad"})
    public void subirNombraRepre() throws InterruptedException, IOException {

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

            entidad.setSolPathNombramientoRepresentante(filePath + File.separator + media.getName());
            System.out.println("PATH SUBIR " + filePath + File.separator + media.getName());
            pdfNombraRepre = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(filePath + File.separator + media.getName()));

        }
    }

    @Command
    @NotifyChange({"pdfAcecptacionNomb", "entidad"})
    public void subirAceptacionNomb() throws InterruptedException, IOException {

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

            entidad.setSolPathAceptacionNombramiento(filePath + File.separator + media.getName());
            System.out.println("PATH SUBIR " + filePath + File.separator + media.getName());
            pdfAcecptacionNomb = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(filePath + File.separator + media.getName()));

        }
    }

    @Command
    @NotifyChange({"pdfRucEmpresa", "entidad"})
    public void subirRucEmpresa() throws InterruptedException, IOException {

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

            entidad.setSolPathRucEmpresa(filePath + File.separator + media.getName());
            System.out.println("PATH SUBIR " + filePath + File.separator + media.getName());
            pdfRucEmpresa = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(filePath + File.separator + media.getName()));

        }
    }

    @Command
    @NotifyChange({"pdfCedRepreEmpresa", "entidad"})
    public void subirCedulaRepre() throws InterruptedException, IOException {

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

            entidad.setSolPathCedulaRepresentanteEmpresa(filePath + File.separator + media.getName());
            System.out.println("PATH SUBIR " + filePath + File.separator + media.getName());
            pdfCedRepreEmpresa = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(filePath + File.separator + media.getName()));

        }
    }

    @Command
    @NotifyChange({"pdfAutoriRepre", "entidad"})
    public void subirAutoRepre() throws InterruptedException, IOException {

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

            entidad.setSolPathAutorizacionRepresentante(filePath + File.separator + media.getName());
            System.out.println("PATH SUBIR " + filePath + File.separator + media.getName());
            pdfAutoriRepre = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(filePath + File.separator + media.getName()));

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

    public AMedia getPdfRuc() {
        return pdfRuc;
    }

    public void setPdfRuc(AMedia pdfRuc) {
        this.pdfRuc = pdfRuc;
    }

    public boolean isRepremiembros() {
        return repremiembros;
    }

    public void setRepremiembros(boolean repremiembros) {
        this.repremiembros = repremiembros;
    }

    public boolean isCargoRepresentate() {
        return cargoRepresentate;
    }

    public void setCargoRepresentate(boolean cargoRepresentate) {
        this.cargoRepresentate = cargoRepresentate;
    }

    public AMedia getPdfConstCompa() {
        return pdfConstCompa;
    }

    public void setPdfConstCompa(AMedia pdfConstCompa) {
        this.pdfConstCompa = pdfConstCompa;
    }

    public AMedia getPdfNombraRepre() {
        return pdfNombraRepre;
    }

    public void setPdfNombraRepre(AMedia pdfNombraRepre) {
        this.pdfNombraRepre = pdfNombraRepre;
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

    public boolean isMienbroEmpresa() {
        return mienbroEmpresa;
    }

    public void setMienbroEmpresa(boolean mienbroEmpresa) {
        this.mienbroEmpresa = mienbroEmpresa;
    }

    public AMedia getPdfAcecptacionNomb() {
        return pdfAcecptacionNomb;
    }

    public void setPdfAcecptacionNomb(AMedia pdfAcecptacionNomb) {
        this.pdfAcecptacionNomb = pdfAcecptacionNomb;
    }

    public AMedia getPdfRucEmpresa() {
        return pdfRucEmpresa;
    }

    public void setPdfRucEmpresa(AMedia pdfRucEmpresa) {
        this.pdfRucEmpresa = pdfRucEmpresa;
    }

    public AMedia getPdfCedRepreEmpresa() {
        return pdfCedRepreEmpresa;
    }

    public void setPdfCedRepreEmpresa(AMedia pdfCedRepreEmpresa) {
        this.pdfCedRepreEmpresa = pdfCedRepreEmpresa;
    }

    public AMedia getPdfAutoriRepre() {
        return pdfAutoriRepre;
    }

    public void setPdfAutoriRepre(AMedia pdfAutoriRepre) {
        this.pdfAutoriRepre = pdfAutoriRepre;
    }

    public List<TipoFirma> getListaTipoFirmas() {
        return listaTipoFirmas;
    }

    public void setListaTipoFirmas(List<TipoFirma> listaTipoFirmas) {
        this.listaTipoFirmas = listaTipoFirmas;
    }

    public List<DetalleTipoFirma> getListaDetalleTipoFirmas() {
        return listaDetalleTipoFirmas;
    }

    public void setListaDetalleTipoFirmas(List<DetalleTipoFirma> listaDetalleTipoFirmas) {
        this.listaDetalleTipoFirmas = listaDetalleTipoFirmas;
    }

    public TipoFirma getTipoFirmaSelected() {
        return tipoFirmaSelected;
    }

    public void setTipoFirmaSelected(TipoFirma tipoFirmaSelected) {
        this.tipoFirmaSelected = tipoFirmaSelected;
    }

}
