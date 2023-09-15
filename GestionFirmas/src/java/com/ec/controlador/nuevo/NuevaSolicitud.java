/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.nuevo;

import com.ec.entidad.Ciudad;
import com.ec.entidad.DetalleTipoFirma;
import com.ec.entidad.EstadoFirma;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.util.Clients;

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
    private boolean campoNoObligatorio = true;
    private boolean tipoUsuario = false;
    
    ServicioParametrizar servicioParametrizar = new ServicioParametrizar();
    private Parametrizar parametrizar = new Parametrizar();
    private Usuario usuario = new Usuario();
    //subir fotografia cedula
    private String filePath;
    byte[] buffer = new byte[2 * 1024 * 1024];
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
    
    private AMedia pdfOtro = null;
    
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
    private boolean solicitudCampos = false;
    private boolean retencion = false;
    private boolean combo = false;
    
    AMedia fileContent = null;
    
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
            tipoFirmaSelected = entidad.getIdDetalleTipoFirma() != null ? entidad.getIdDetalleTipoFirma().getIdTipoFirma() : null;
            if (tipoFirmaSelected != null) {
                listaDetalleTipoFirmas = servicioDetalleTipoFirma.findByTipoFirma(tipoFirmaSelected);
            }
            
            if (entidad.getSolNumRetencion() != null) {
                retencion = true;
            }
            
            cargarVistaTiposSol(valor.getSolTipo() == null ? "" : valor.getSolTipo());
            System.out.println(usuario.getUsuNivel());
            if (usuario.getUsuNivel() == 3) {
                tipoUsuario = true;
            }
        } else {
            System.out.println(usuario.getUsuNivel());
            //muestra 7 dias atras
            Calendar calendar = Calendar.getInstance(); //obtiene la fecha de hoy 
            calendar.add(Calendar.YEAR, -18); //el -3 indica que se le restaran 3 dias 

            entidad.setSolConRuc(false);
            this.entidad = new Solicitud();
            this.entidad.setSolFechaNacimiento(calendar.getTime());
            this.entidad.setSolTipo("PN");
            setMienbroEmpresa(false);
            accion = "create";
            
        }
        conRuc();
        obtenerEdad();
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
            if (entidad.getSolPathOtroPdf() != null) {
                try {
                    System.out.println("PDF constitucion getSolPathOtroPdf" + entidad.getSolPathOtroPdf());
                    pdfOtro = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(entidad.getSolPathOtroPdf()));
                    
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
    
    @NotifyChange({"listaCiudades", "provinciaSelected", "entidad"})
    public void conRuc() {
        if (entidad.getSolConRuc()) {
            entidad.setSolRuc(entidad.getSolCedula() + "001");
            
        } else {
            entidad.setSolRuc(entidad.getSolCedula() != null ? entidad.getSolCedula() : "");
            
        }
        
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
    @NotifyChange({"listaDatos", "tipoFirmaSelected"})
    public void guardar() {
        camposVaciosPorSolicitud();
        
        if (solicitudCampos) {
//            entidad.setUsuNivel(Integer.valueOf(usuNivel));
            if (accion.equals("create")) {
                entidad.setIdEstadoProceso(servicioEstadoProceso.findBySigla("ING"));
                entidad.setIdUsuario(usuario);
                entidad.setSolFechaCreacion(new Date());
                entidad.setSolTipo(tipoSolicitud);
                EstadoFirma idEstadoFirma = new EstadoFirma();
                idEstadoFirma.setIdEstadoFirma(3);
                entidad.setIdEstadoFirma(idEstadoFirma);
                servicio.crear(entidad);
                wSolicitud.detach();
                sweetAltert("success", "OK", "Solicitud creada con éxito");
            } else {
                if (entidad.getIdEstadoProceso() == null) {
                    entidad.setIdEstadoProceso(servicioEstadoProceso.findBySigla("ING"));
                }
                entidad.setIdEstadoProceso(servicioEstadoProceso.findBySigla("ING"));
                servicio.modificar(entidad);
                wSolicitud.detach();
                sweetAltert("success", "OK", "Solicitud editada con éxito");
            }
            
        }
    }
    
    @Command
    public void aprobar(@BindingParam("valor") int valor) {
        camposVaciosPorSolicitud();
        System.out.println(solicitudCampos);
        if (solicitudCampos) {
            if (entidad.getIdEstadoProceso() == null) {
                entidad.setIdEstadoProceso(servicioEstadoProceso.findBySigla("ING"));
            }
            //entidad.setIdEstadoProceso(servicioEstadoProceso.findBySigla("ING"));
            entidad.getIdEstadoProceso().setIdEstadoProceso(valor);
            if (valor == 2) {
                entidad.setSolFechaSolicitudAprobacion(new Date());
            }
            servicio.modificar(entidad);
            wSolicitud.detach();
            sweetAltert("success", "OK", "Solicitud editada con éxito");
            
        }
    }
    
    public void camposVaciosPorSolicitud() {
        tipoSolicitud = entidad.getSolTipo();
        
        switch (tipoSolicitud) {
            case "PN":
                solicitudCampos = verificarCamposVacios("PN");
                break;
            case "RLE":
                solicitudCampos = verificarCamposVacios("RLE");
                break;
            case "ME":
                solicitudCampos = verificarCamposVacios("ME");
                break;
            default:
                System.out.println("Opción no válida");
                break;
        }
    }
    
    public boolean verificarCamposVacios(String tipoSol) {
        
        if (entidad.getSolTipoDocumento() == null
                || entidad.getSolRuc() == null
                || entidad.getSolCodigoDactilar() == null
                || entidad.getSolFechaNacimiento() == null
                || entidad.getSolNombre() == null
                || entidad.getSolApellido1() == null
                || entidad.getSolMail() == null
                || entidad.getSolCelular() == null
                || entidad.getSolSexo() == null
                || entidad.getSolConRuc() == null
                || entidad.getIdDetalleTipoFirma() == null
                || provinciaSelected == null
                || entidad.getIdCiudad().getCiuNombre() == null
                || entidad.getSolDireccionCompleta() == null
                || fotoGeneral == null
                || fotoReverso == null
                || fotoSelfi == null) {
            
            sweetAltert("warning", "Datos faltantes", "Todos los campos con (*) son importantes, revice los siguientes apartados: Datos Personales, Direccion domicilio, Documentos personales(todos los documentos son obligatorios), Firma");
            return false;
        } else {
            if (tipoSol.equals("RLE")) {
                if (entidad.getSolRucEmpresa() == null
                        || entidad.getSolRazonSocial() == null
                        || entidad.getSolCargoRepresentante() == null
                        || pdfConstCompa == null
                        || pdfNombraRepre == null
                        || pdfAcecptacionNomb == null) {
                    sweetAltert("warning", "Datos faltantes", "Todos los campos con (*) son importantes, revice los siguientes apartados: Datos de la empresa, Documentos");
                    return false;
                }
            } else if (tipoSol.equals("ME")) {
                if (entidad.getSolRucEmpresa() == null
                        || entidad.getSolRazonSocial() == null
                        || entidad.getSolArea() == null
                        || entidad.getSolCargoSolicitante() == null
                        || entidad.getSolTipoDocumento() == null
                        || entidad.getSolMeRuc() == null
                        || entidad.getSolMeNombres() == null
                        || entidad.getSolApellido1() == null
                        || pdfConstCompa == null
                        || pdfNombraRepre == null
                        || pdfAcecptacionNomb == null
                        || pdfRucEmpresa == null
                        || pdfCedRepreEmpresa == null
                        || pdfAutoriRepre == null) {
                    sweetAltert("warning", "Datos faltantes", "Todos los campos con (*) son importantes, revice los siguientes apartados: Datos de la empresa, Representante legal, Docmuentos");
                    return false;
                }
            }
        }
        return true;
    }
    
    public void sweetAltert(String alertaTipo, String tituloMensaje, String detalleMensaje) {
        String script = "Swal.fire(\n"
                + "  '" + tituloMensaje + "',\n"
                + "  '" + detalleMensaje + "',\n"
                + "  '" + alertaTipo + "'\n"
                + ")";
        System.out.println(script);
        Clients.evalJavaScript(script);
    }
    
    @Command
    public void verPDF(@BindingParam("valor") String tipoarchivo) {
        try {
            
            File f = new File("");
            buffer = new byte[2 * 1024 * 1024];
            if (tipoarchivo.equals("ruc")) {
                f = new File(entidad.getSolPathRuc());
            } else if (tipoarchivo.equals("CNC")) {
                f = new File(entidad.getSolPathConstitucionCompania());
            } else if (tipoarchivo.equals("AN")) {
                f = new File(entidad.getSolPathAceptacionNombramiento());
            } else if (tipoarchivo.equals("CRE")) {
                f = new File(entidad.getSolPathCedulaRepresentanteEmpresa());
            } else if (tipoarchivo.equals("AR")) {
                f = new File(entidad.getSolPathAutorizacionRepresentante());
            } else if (tipoarchivo.equals("NR")) {
                f = new File(entidad.getSolPathNombramientoRepresentante());
            } else if (tipoarchivo.equals("RE")) {
                f = new File(entidad.getSolPathRucEmpresa());
            } else if (tipoarchivo.equals("otro")) {
                f = new File(entidad.getSolPathOtroPdf());
            }
            FileInputStream fs = new FileInputStream(f);
            fs.read(buffer);
            fs.close();
            ByteArrayInputStream is = new ByteArrayInputStream(buffer);
            AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", is);
            fileContent = amedia;
            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
//para pasar al visor
            map.put("pdf", fileContent);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/visor/visorreporte.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            sweetAltert("error", "Error visualización", "No existe el archivo");
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
    @NotifyChange({"mienbroEmpresa", "repremiembros", "campoNoObligatorio", "cargoRepresentate", "entidad", "combo"})
    public void personaNatural() {
        cargarVistaTiposSol("PN");
        entidad.setSolConRuc(true);
        combo = false;
    }
    
    @Command
    @NotifyChange({"mienbroEmpresa", "repremiembros", "campoNoObligatorio", "cargoRepresentate", "entidad", "combo"})
    public void repreLegalEmpresa() {
        cargarVistaTiposSol("RLE");
        entidad.setSolConRuc(true);
        combo = true;
    }
    
    @Command
    @NotifyChange({"mienbroEmpresa", "repremiembros", "campoNoObligatorio", "cargoRepresentate", "entidad", "combo"})
    public void miembEmpresa() {
        cargarVistaTiposSol("ME");
        entidad.setSolConRuc(true);
        combo = true;
    }
    
    public void cargarVistaTiposSol(String tipoSol) {
        if (tipoSol.equals("PN")) {
            mienbroEmpresa = false;
            repremiembros = false;
            cargoRepresentate = true;
            campoNoObligatorio = true;
        } else if (tipoSol.equals("RLE") || tipoSol.equals("ME")) {
            if (tipoSol.equals("RLE")) {
                mienbroEmpresa = false;
                repremiembros = true;
                cargoRepresentate = true;
                campoNoObligatorio = false;
            } else if (tipoSol.equals("ME")) {
                repremiembros = true;
                mienbroEmpresa = true;
                cargoRepresentate = false;
                campoNoObligatorio = false;
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
    
    public AImage getFotoGeneral() {
        return fotoGeneral;
    }
    
    public void setFotoGeneral(AImage fotoGeneral) {
        this.fotoGeneral = fotoGeneral;
    }
    
    public boolean isRetencion() {
        return retencion;
    }
    
    public void setRetencion(boolean retencion) {
        this.retencion = retencion;
    }
    
    public boolean isTipoUsuario() {
        return tipoUsuario;
    }
    
    public void setTipoUsuario(boolean tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
    public boolean isCombo() {
        return combo;
    }
    
    public void setCombo(boolean combo) {
        this.combo = combo;
    }
    
    @Command
    @NotifyChange({"pdfOtro", "entidad"})
    public void subirOtroPdf() throws InterruptedException, IOException {
        
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
            
            entidad.setSolPathOtroPdf(filePath + File.separator + media.getName());
            System.out.println("PATH SUBIR " + filePath + File.separator + media.getName());
            pdfOtro = new AMedia("report", "pdf", "application/pdf", Imagen_A_Bytes(filePath + File.separator + media.getName()));
            
        }
    }
    
    public AMedia getPdfOtro() {
        return pdfOtro;
    }
    
    public void setPdfOtro(AMedia pdfOtro) {
        this.pdfOtro = pdfOtro;
    }
    
    public boolean isCampoNoObligatorio() {
        return campoNoObligatorio;
    }
    
    public void setCampoNoObligatorio(boolean campoNoObligatorio) {
        this.campoNoObligatorio = campoNoObligatorio;
    }
    
}
