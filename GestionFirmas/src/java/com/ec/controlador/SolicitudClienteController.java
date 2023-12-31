/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.EstadoFirma;
import com.ec.entidad.Solicitud;
import com.ec.entidad.Usuario;
import com.ec.entidad.EstadoProceso;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.HelperPersistencia;
import com.ec.servicio.ServicioEstadoProceso;
import com.ec.servicio.ServicioSolicitud;

import com.ec.servicio.ServicioUsuario;
import com.ec.utilitario.MailerClass;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.internet.ParseException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author Darwin
 */
public class SolicitudClienteController {

    private Usuario usuario;
    ServicioUsuario servicioUsuario = new ServicioUsuario();
    UserCredential credential = new UserCredential();
    ServicioSolicitud servicioSolicitud = new ServicioSolicitud();
    ServicioEstadoProceso servicioEstadoProceso = new ServicioEstadoProceso();
    private List<Solicitud> listaDatos = new ArrayList<Solicitud>();
    private List<EstadoProceso> listadoEstados = new ArrayList<EstadoProceso>();
    private EstadoProceso tipoSolSelected = null;
    private String buscar = "";
    private Date fechainicio = new Date();
    private Date fechafin = new Date();
    //subir pdf
    private String filePath;
    byte[] buffer = new byte[1024 * 1024];
    private AImage fotoGeneral = null;

       
    Connection con = null;
    //reporte
    AMedia fileContent = null;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, JRException, IOException {
        Selectors.wireComponents(view, this, false);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechainicio);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        fechainicio = calendar.getTime();
        fechainicio = fechaFormateada("inicio", fechainicio);
        fechafin = fechaFormateada("fin", fechafin);

        System.out.println(fechafin);
        System.out.println(fechainicio);
        buscarSolicitudes();
        //cargarDatos();
    }

    private void cargarDatos() {
        listadoEstados = servicioEstadoProceso.finAll();
    }

    public SolicitudClienteController() {

        Session sess = Sessions.getCurrent();
        credential = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());

        buscar = credential.getUsuarioSistema().getUsuLogin();
        buscarSolicitudes();

    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void buscarLike() {

        listaDatos = servicioSolicitud.findLikeSolicitudCliente(credential.getUsuarioSistema().getUsuRuc());
    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void eliminarPaciente(@BindingParam("valor") Solicitud valor) {
        try {
            if (Messagebox.show("¿Esta seguro de eliminar el paciente?", "Atención", Messagebox.YES | Messagebox.NO, Messagebox.INFORMATION) == Messagebox.YES) {

                servicioSolicitud.modificar(valor);
                buscarLike();
            }
        } catch (Exception e) {
            Clients.showNotification("Ocurrio un error " + e.getMessage(),
                        Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
        }
    }

    @Command
    public void reporteExel() throws IOException, ParseException {
        try {
            File dosfile = new File(exportarExcel());
            if (dosfile.exists()) {
                FileInputStream inputStream = new FileInputStream(dosfile);
                Filedownload.save(inputStream, new MimetypesFileTypeMap().getContentType(dosfile), dosfile.getName());
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR AL DESCARGAR EL ARCHIVO" + e.getMessage());
        }
    }

    private String exportarExcel() throws FileNotFoundException, IOException, ParseException {
        String directorioReportes = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/reportes");

        String pathSalida = directorioReportes + File.separator + "solicitudes.xls";

        try {
            int j = 0;
            File archivoXLS = new File(pathSalida);
            if (archivoXLS.exists()) {
                archivoXLS.delete();
            }
            archivoXLS.createNewFile();
            FileOutputStream archivo = new FileOutputStream(archivoXLS);
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet s = wb.createSheet("Emitidas");

            HSSFFont fuente = wb.createFont();
            fuente.setBoldweight((short) 700);
            HSSFCellStyle estiloCelda = wb.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment((short) 2);
            estiloCelda.setFont(fuente);

            HSSFCellStyle estiloCeldaInterna = wb.createCellStyle();
            estiloCeldaInterna.setWrapText(true);
            estiloCeldaInterna.setAlignment((short) 5);
            estiloCeldaInterna.setFont(fuente);

            HSSFCellStyle estiloCelda1 = wb.createCellStyle();
            estiloCelda1.setWrapText(true);
            estiloCelda1.setFont(fuente);

            HSSFRow r = null;

            HSSFCell c = null;
            r = s.createRow(0);

            HSSFCell chfe = r.createCell(j++);
            chfe.setCellValue(new HSSFRichTextString("Tipo Documento"));
            chfe.setCellStyle(estiloCelda);

            HSSFCell chfe1 = r.createCell(j++);
            chfe1.setCellValue(new HSSFRichTextString("RUC/Cedula"));
            chfe1.setCellStyle(estiloCelda);

            HSSFCell chfe12 = r.createCell(j++);
            chfe12.setCellValue(new HSSFRichTextString("Codigo Dactilar"));
            chfe12.setCellStyle(estiloCelda);

            HSSFCell ch3 = r.createCell(j++);
            ch3.setCellValue(new HSSFRichTextString("Fecha Nacimiento"));
            ch3.setCellStyle(estiloCelda);

            HSSFCell ch4 = r.createCell(j++);
            ch4.setCellValue(new HSSFRichTextString("Edad"));
            ch4.setCellStyle(estiloCelda);

            HSSFCell ch6 = r.createCell(j++);
            ch6.setCellValue(new HSSFRichTextString("Nombres"));
            ch6.setCellStyle(estiloCelda);

            HSSFCell ch7 = r.createCell(j++);
            ch7.setCellValue(new HSSFRichTextString("1er Apellido"));
            ch7.setCellStyle(estiloCelda);

            HSSFCell ch8 = r.createCell(j++);
            ch8.setCellValue(new HSSFRichTextString("2do Apellido"));
            ch8.setCellStyle(estiloCelda);

            HSSFCell ch9 = r.createCell(j++);
            ch9.setCellValue(new HSSFRichTextString("Email"));
            ch9.setCellStyle(estiloCelda);

            HSSFCell ch10 = r.createCell(j++);
            ch10.setCellValue(new HSSFRichTextString("Celular"));
            ch10.setCellStyle(estiloCelda);

            HSSFCell ch11 = r.createCell(j++);
            ch11.setCellValue(new HSSFRichTextString("Email 2"));
            ch11.setCellStyle(estiloCelda);

            HSSFCell ch12 = r.createCell(j++);
            ch12.setCellValue(new HSSFRichTextString("Cedular 2"));
            ch12.setCellStyle(estiloCelda);

            HSSFCell ch13 = r.createCell(j++);
            ch13.setCellValue(new HSSFRichTextString("Sexo"));
            ch13.setCellStyle(estiloCelda);

            HSSFCell ch14 = r.createCell(j++);
            ch14.setCellValue(new HSSFRichTextString("Con RUC"));
            ch14.setCellStyle(estiloCelda);

            HSSFCell ch15 = r.createCell(j++);
            ch15.setCellValue(new HSSFRichTextString("Provincia"));
            ch15.setCellStyle(estiloCelda);

            HSSFCell ch16 = r.createCell(j++);
            ch16.setCellValue(new HSSFRichTextString("Ciudad"));
            ch16.setCellStyle(estiloCelda);

            HSSFCell ch17 = r.createCell(j++);
            ch17.setCellValue(new HSSFRichTextString("Direccion"));
            ch17.setCellStyle(estiloCelda);

            HSSFCell ch18 = r.createCell(j++);
            ch18.setCellValue(new HSSFRichTextString("Estado Solicitud"));
            ch18.setCellStyle(estiloCelda);

            HSSFCell ch19 = r.createCell(j++);
            ch19.setCellValue(new HSSFRichTextString("Estado Firma"));
            ch19.setCellStyle(estiloCelda);

            int rownum = 1;
            int i = 0;

            for (Solicitud item : listaDatos) {
                i = 0;

                r = s.createRow(rownum);

                HSSFCell cf1 = r.createCell(i++);
                cf1.setCellValue(new HSSFRichTextString(item.getSolTipoDocumento()));

                HSSFCell cf2 = r.createCell(i++);
                cf2.setCellValue(new HSSFRichTextString(item.getSolRuc()));

                HSSFCell cf3 = r.createCell(i++);
                cf3.setCellValue(new HSSFRichTextString(item.getSolCodigoDactilar()));

                HSSFCell cf4 = r.createCell(i++);
                cf4.setCellValue(new HSSFRichTextString(item.getSolFechaNacimiento().toString()));

                HSSFCell cf5 = r.createCell(i++);
                cf5.setCellValue(new HSSFRichTextString(item.getSolEdad().toString()));

                HSSFCell cf6 = r.createCell(i++);
                cf6.setCellValue(new HSSFRichTextString(item.getSolNombre()));

                HSSFCell cf7 = r.createCell(i++);
                cf7.setCellValue(new HSSFRichTextString(item.getSolApellido1()));

                HSSFCell cf8 = r.createCell(i++);
                cf8.setCellValue(new HSSFRichTextString(item.getSolApellido2()));

                HSSFCell cf9 = r.createCell(i++);
                cf9.setCellValue(new HSSFRichTextString(item.getSolMail()));

                HSSFCell cf10 = r.createCell(i++);
                cf10.setCellValue(new HSSFRichTextString(item.getSolCelular()));

                HSSFCell cf12 = r.createCell(i++);
                cf12.setCellValue(new HSSFRichTextString(item.getSolMailOp()));

                HSSFCell cf13 = r.createCell(i++);
                cf13.setCellValue(new HSSFRichTextString(item.getSolCelularOp()));

                HSSFCell cf14 = r.createCell(i++);
                cf14.setCellValue(new HSSFRichTextString(item.getSolSexo()));

                HSSFCell cf15 = r.createCell(i++);
                cf15.setCellValue(new HSSFRichTextString(item.getSolConRuc() ? "SI" : "NO"));

                HSSFCell cf16 = r.createCell(i++);
                cf16.setCellValue(new HSSFRichTextString(item.getIdCiudad().getIdProvincia().getProvNombre()));

                HSSFCell cf17 = r.createCell(i++);
                cf17.setCellValue(new HSSFRichTextString(item.getIdCiudad().getCiuNombre()));

                HSSFCell cf18 = r.createCell(i++);
                cf18.setCellValue(new HSSFRichTextString(item.getSolDireccionCompleta()));

                HSSFCell cf19 = r.createCell(i++);
                cf19.setCellValue(new HSSFRichTextString(item.getIdEstadoProceso().getEstDescripcion()));

                HSSFCell cf20 = r.createCell(i++);
                cf20.setCellValue(new HSSFRichTextString(item.getIdEstadoFirma() == null ? " " : " "));

                /*autemta la siguiente fila*/
                rownum += 1;

            }

            for (int k = 0; k <= listaDatos.size(); k++) {
                s.autoSizeColumn(k);
            }

            wb.write(archivo);
            archivo.close();

        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }
        return pathSalida;

    }

    @Command
    public void pdfSolicitudAll() {

        EntityManager emf = HelperPersistencia.getEMF();

        try {
            emf.getTransaction().begin();
            con = emf.unwrap(Connection.class);

            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                        .getRealPath("/reportes");
            String reportPath = "";

            reportPath = reportFile + File.separator + "listadosolicitudAll.jasper";

            Map<String, Object> parametros = new HashMap<String, Object>();

            //  parametros.put("codUsuario", String.valueOf(credentialLog.getAdUsuario().getCodigoUsuario()));
            parametros.put("busqueda", buscar);

            if (con != null) {
                System.out.println("Conexión Realizada Correctamenteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            }
            FileInputStream is = null;
            is = new FileInputStream(reportPath);

            byte[] buf = JasperRunManager.runReportToPdf(is, parametros, con);
            InputStream mediais = new ByteArrayInputStream(buf);
            AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", mediais);
            fileContent = amedia;
            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
//para pasar al visor
            map.put("pdf", fileContent);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/modal/visorreporte.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            System.out.println("ERROR EL PRESENTAR EL REPORTE " + e.getMessage());
        } finally {
            if (emf != null) {
                emf.getTransaction().commit();
            }

        }

    }

    @Command
    public void pdfSolicitudFecha() {

        EntityManager emf = HelperPersistencia.getEMF();

        try {
            emf.getTransaction().begin();
            con = emf.unwrap(Connection.class);

            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                        .getRealPath("/reportes");
            String reportPath = "";

            reportPath = reportFile + File.separator + "listadosolicitudGeneralFecha.jasper";

            Map<String, Object> parametros = new HashMap<String, Object>();

            //  parametros.put("codUsuario", String.valueOf(credentialLog.getAdUsuario().getCodigoUsuario()));
            parametros.put("busqueda", buscar);
            parametros.put("inicio", fechainicio);
            parametros.put("fin", fechafin);

            if (con != null) {
                System.out.println("Conexión Realizada Correctamenteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            }
            FileInputStream is = null;
            is = new FileInputStream(reportPath);

            byte[] buf = JasperRunManager.runReportToPdf(is, parametros, con);
            InputStream mediais = new ByteArrayInputStream(buf);
            AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", mediais);
            fileContent = amedia;
            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
//para pasar al visor
            map.put("pdf", fileContent);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/modal/visorreporte.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            System.out.println("ERROR EL PRESENTAR EL REPORTE " + e.getMessage());
        } finally {
            if (emf != null) {
                emf.getTransaction().commit();
            }

        }

    }

    @Command
    public void pdfSolicitudIndividual(@BindingParam("valor") Solicitud valor) {

        EntityManager emf = HelperPersistencia.getEMF();

        try {
            emf.getTransaction().begin();
            con = emf.unwrap(Connection.class);

            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                        .getRealPath("/reportes");
            String reportPath = "";

            if (valor.getSolTipo().equals("PN")) {
                reportPath = reportFile + File.separator + "solicitudIndividual.jasper";
            } else if (valor.getSolTipo().equals("RLE")) {
                reportPath = reportFile + File.separator + "solicitudIndividualRLE.jasper";
            } else if (valor.getSolTipo().equals("ME")) {
                reportPath = reportFile + File.separator + "solicitudIndividuaME.jasper";
            }

            Map<String, Object> parametros = new HashMap<String, Object>();

            //  parametros.put("codUsuario", String.valueOf(credentialLog.getAdUsuario().getCodigoUsuario()));
            parametros.put("idSolicitud", valor.getIdSolicitud());

            if (con != null) {
                System.out.println("Conexión Realizada Correctamenteee");
            }
            FileInputStream is = null;
            is = new FileInputStream(reportPath);

            byte[] buf = JasperRunManager.runReportToPdf(is, parametros, con);
            InputStream mediais = new ByteArrayInputStream(buf);
            AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", mediais);
            fileContent = amedia;
            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
//para pasar al visor
            map.put("pdf", fileContent);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/modal/visorreporte.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            System.out.println("ERROR EL PRESENTAR EL REPORTE " + e.getMessage());
        } finally {
            if (emf != null) {
                emf.getTransaction().commit();
            }

        }

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

    public Date fechaFormateada(String tipo, Date fecha) {
        Date fechaActual = fecha;

        // Crear un objeto SimpleDateFormat para el formato de la hora
        String horaEspecificaStr = "00:00:00";
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");

        if (tipo.equals("fin")) {
            horaEspecificaStr = "23:59:59";
        }

        try {
            // Obtener la hora específica como una cadena

            // Obtener la fecha actual como una cadena en formato "yyyy-MM-dd"
            String fechaActualStr = new SimpleDateFormat("yyyy-MM-dd").format(fechaActual);

            // Concatenar la fecha y la hora
            String fechaHoraConcatenadaStr = fechaActualStr + " " + horaEspecificaStr;

            // Crear un objeto SimpleDateFormat para el formato de fecha y hora
            SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // Parsear la cadena a un objeto Date
            Date fechaHoraConcatenada = formatoFechaHora.parse(fechaHoraConcatenadaStr);

            return fechaHoraConcatenada;
        } catch (Exception e) {
            e.printStackTrace();
            return fechaActual;
        }
    }

   

    private void buscarSolicitudes() {
        listaDatos = servicioSolicitud.findLikeSolicitudCliente(buscar);

    }

    public List<Solicitud> getListaDatos() {
        return listaDatos;
    }

    public void setListaDatos(List<Solicitud> listaDatos) {
        this.listaDatos = listaDatos;
    }

    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }

    public Date getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(Date fechainicio) {
        this.fechainicio = fechainicio;
    }

    public Date getFechafin() {
        return fechafin;
    }

    public void setFechafin(Date fechafin) {
        this.fechafin = fechafin;
    }
    
     @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void generarFirma(@BindingParam("valor") Solicitud valor) {
         try {
//            if (Messagebox.show("¿Desea modificar el registro, recuerde que debe crear las reteniones nuevamente?", "Atención", Messagebox.YES | Messagebox.NO, Messagebox.INFORMATION) == Messagebox.YES) {
            final HashMap<String, Solicitud> map = new HashMap<String, Solicitud>();
            map.put("valor", valor);
                org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                        "/cliente/nuevo/descargar.zul", null, map);
                window.doModal();
            buscarLike();;

        } catch (Exception e) {
            Clients.showNotification("Ocurrio un error " + e.getMessage(),
                    Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 2000, true);
        }
    }

}
