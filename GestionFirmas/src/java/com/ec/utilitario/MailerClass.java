/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.utilitario;


import com.ec.entidad.Parametrizar;
import com.ec.servicio.ServicioParametrizar;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.BodyPart;
import javax.mail.Transport;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.MimeUtility;

/**
 * Clase que permite el envio de e-mails utilizando el API javamail.
 *
 */
public class MailerClass {

    private Parametrizar parametrizar = new Parametrizar();
    ServicioParametrizar servicioParametrizar = new ServicioParametrizar();

    /**
     * Recupera el nombre del catálogo descrito en la enumeración
     *
     * @param categoria nombre del parametroa a buscar
     * @return
     */
    public String getConfiguracionCorreo(String categoria) {
//        Set<BeCatalogo> dato = ofertaServicio.getCatalogo1(categoria);
//        if (dato.iterator().hasNext()) {
//            return dato.iterator().next().getNbCatalogo();
//        }
        return null;
    }

    /**
     * Método que envía al mail las credenciales de acceso al sistema
     *
     * @param address Dirección de correo electronico
     * @param mensaje Contenido del mensaje
     * @return
     * @throws java.rmi.RemoteException
     */
//    public boolean sendMail(List<String> address, String mensaje,
//            String pathAdjunto, String asuntoInf,
//            String nombreArchivoMail, String rutaFTP)
//            throws java.rmi.RemoteException {
//
//        try {
//            System.out.println("INGRESA AL ENVIO");
//            String asunto = asuntoInf;
//            String host = "smtp.gmail.com";
//            String port = "587";
//            String from = "imdiquito@gmail.com";
//            String protocol = "smtp";
//            String usuarioSmpt = "imdiquito@gmail.com";
//            String password = "mspmsp506";
//
//            // Propiedades de la conexión
//            // Get system properties
//            Properties properties = System.getProperties();
//
//            // Setup mail server
//            properties.setProperty("mail.smtp.host", host);
//            properties.setProperty("mail.smtp.user", usuarioSmpt);
//            properties.setProperty("mail.smtp.password", password);
//            properties.setProperty("mail.smtp.port", port);
//            properties.setProperty("mail.smtp.starttls.enable", "true");
//            properties.setProperty("mail.smtp.auth", "true");
//            properties.setProperty("mail.debug", "false");
//            // Setup Port
//            properties.put("mail.smtp.ssl.trust", host);
//            SmtpAuthenticator auth = new SmtpAuthenticator();
//            // Get the default Session object.
//            Session session = Session.getInstance(properties, auth);
//            MimeMessage m = new MimeMessage(session);
//            Address addressfrom = new InternetAddress();
//            InternetAddress[] recipientAddress = new InternetAddress[address.size()];
//            int count = 0;
//            for (String item : address) {
//                recipientAddress[count] = new InternetAddress(item.trim());
//                count++;
//            }
//
//            Address[] addresTto = recipientAddress;
//
//            m.setFrom(addressfrom);
//
//            BodyPart texto = new MimeBodyPart();
////            texto.setText("Informacion que  se desee enviar");
//            String linkFacebook = "https://www.facebook.com/Imagen.Digital.Impresiones/timeline";
//            String linkPagina = "http://www.imagenec.com/";
//
//            texto.setContent("<h4>" + mensaje + "</h4><br>"
//                    + "<IMG SRC='" + rutaFTP + "'>"
//                    + "<table>\n"
//                    + "	<tr>\n"
//                    + "	<td>Visitanos en nuestra página oficial:\n"
//                    + "	</td>\n"
//                    + "	<td>\n"
//                    + "	<a href=" + linkPagina + ">  " + linkPagina + " </a>\n"
//                    + "	</td>\n"
//                    + "	</tr>\n"
//                    + "	\n"
//                    + "    <tr>\n"
//                    + "	<td>Visitanos en facebook:</td>\n"
//                    + "	<td>\n"
//                    + "	<a href=" + linkFacebook + "> " + linkFacebook + "</a>\n"
//                    + "	</td></tr>\n"
//                    + " <tr>\n"
//                    + " <td>Contactenos:</td>\n"
//                    + "	<td>Pontevedra N24-275 entre Guipuzcoa y Vizcaya (A una cuadra del Cine Ocho y Medio, sector Floresta)\n"
//                    + "Quito</td>"
//                    + "</tr>\n"
//                    + " <tr>\n"
//                    + "	<td>Telefax:</td><td> (593 2) 2 904 639</td>\n"
//                    + "</tr>\n"
//                    + " <tr>\n"
//                    + "	<td>Movil:</td><td> (593 2) 9982 37 099 / 098 3515 718</td>\n"
//                    + "</tr>\n"
//                    + " <tr>\n"
//                    + "	<td>Ventas: </td><td>ventas@imagenec.com</td>\n"
//                    + "</tr>\n"
//                    + " <tr>\n"
//                    + "	<td>Gerencia:</td><td> barlin@imagenec.com</td>\n"
//                    + "</tr>\n"
//                    + "</table>", "text/html");
//
//            MimeMultipart multiParte = new MimeMultipart();
//            // inicio adjunto
//
//            if (pathAdjunto.equals("")) {
//            } else {
//                BodyPart adjunto = new MimeBodyPart();
//                adjunto.setDataHandler(new DataHandler(new FileDataSource(
//                        pathAdjunto)));
//                adjunto.setFileName(nombreArchivoMail);
//                multiParte.addBodyPart(adjunto);
//            }
//
//            multiParte.addBodyPart(texto);
//
//            m.setRecipients(Message.RecipientType.TO, addresTto);
////            m.setRecipients(Message.RecipientType.BCC, from);
//            m.setSubject(asunto);
//            m.setSentDate(new java.util.Date());
////             m.setContent(dirDatos, "text/plain");
//            m.setContent(multiParte);
//
//            Transport t = session.getTransport(protocol);
////             t.connect();
//            t.connect(host, usuarioSmpt, password);
//            t.send(m);
//            t.close();
//            return true;
//        } catch (javax.mail.MessagingException e) {
//            System.out.println("error" + e);
//            e.printStackTrace();
//
//            return false;
//        }
//    }

    class SmtpAuthenticator extends Authenticator {

        public SmtpAuthenticator() {

            super();
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            parametrizar = servicioParametrizar.findActivo();
            String username = parametrizar.getParCorreo().trim();
            String password = parametrizar.getParContrasena().trim();

            return new PasswordAuthentication(username, password);

        }
    }

    //envio de mail simple
//    
//      m.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse(address));
    public boolean sendMailSimple(String address, String mensaje,
            String[] attachFiles, String asuntoInf)
            throws java.rmi.RemoteException, UnsupportedEncodingException {

        try {
//                        String usuarioSmpt = "deckxelec@gmail.com";
//            String password = "metalicas366";

            parametrizar = servicioParametrizar.findActivo();

//            String asunto = asuntoInf;
//            String host = "smtp.gmail.com";
//            String port = "587";
//            String protocol = "smtp";
//            String usuarioSmpt = "deckxelec@gmail.com";
//            String password = "Dereckandre02";
            String asunto = asuntoInf;
            String host = parametrizar.getParHost();
            String port = parametrizar.getParPuerto().toString();
            String protocol = parametrizar.getParaProtocolo();
            String usuarioSmpt = parametrizar.getParCorreo().trim();
            String password = parametrizar.getParContrasena().trim();

            // Propiedades de la conexión
            // Get system properties
            Properties properties = System.getProperties();

            // Setup mail server
            properties.setProperty("mail.smtp.host", host);
            properties.setProperty("mail.smtp.user", usuarioSmpt);
            properties.setProperty("mail.smtp.password", password);
            properties.setProperty("mail.smtp.port", port);
            properties.setProperty("mail.smtp.starttls.enable", "true");
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.debug", "false");
            // Setup Port
            properties.put("mail.smtp.ssl.trust", host);
            SmtpAuthenticator auth = new SmtpAuthenticator();
            // Get the default Session object.
            Session session = Session.getInstance(properties, auth);
            MimeMessage m = new MimeMessage(session);
              String nickFrom = MimeUtility.encodeText("Consultorio Medico");
               Address addressfrom = new InternetAddress(usuarioSmpt, nickFrom);

            m.setFrom(addressfrom);

            BodyPart texto = new MimeBodyPart();
//            texto.setText("Informacion que  se desee enviar");
//            String linkFacebook = "https://www.facebook.com/papeleriacom/?fref=ts";
//            String linkPortal = "http://186.4.130.202:8080/ventas/portal/inicio.zul";
//            String nombrePortal = "Portal para sus facturas electonicas";

             String HTMLENVIO = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n"
                    + "<head>\n"             
                    + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                    + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                    + "  <meta name=\"x-apple-disable-message-reformatting\">\n"
                    + "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"
                    + "  <title>"+parametrizar.getParDescripcion()+"</title>\n"
                    + "\n"
                    + "  <style type=\"text/css\">\n"
                    + "    table,\n"
                    + "    td {\n"
                    + "      color: #000000;\n"
                    + "    }\n"
                    + "    \n"
                    + "    @media only screen and (min-width: 620px) {\n"
                    + "      .u-row {\n"
                    + "        width: 600px !important;\n"
                    + "      }\n"
                    + "      .u-row .u-col {\n"
                    + "        vertical-align: top;\n"
                    + "      }\n"
                    + "      .u-row .u-col-100 {\n"
                    + "        width: 600px !important;\n"
                    + "      }\n"
                    + "    }\n"
                    + "    \n"
                    + "    @media (max-width: 620px) {\n"
                    + "      .u-row-container {\n"
                    + "        max-width: 100% !important;\n"
                    + "        padding-left: 0px !important;\n"
                    + "        padding-right: 0px !important;\n"
                    + "      }\n"
                    + "      .u-row .u-col {\n"
                    + "        min-width: 320px !important;\n"
                    + "        max-width: 100% !important;\n"
                    + "        display: block !important;\n"
                    + "      }\n"
                    + "      .u-row {\n"
                    + "        width: calc(100% - 40px) !important;\n"
                    + "      }\n"
                    + "      .u-col {\n"
                    + "        width: 100% !important;\n"
                    + "      }\n"
                    + "      .u-col>div {\n"
                    + "        margin: 0 auto;\n"
                    + "      }\n"
                    + "    }\n"
                    + "    \n"
                    + "    body {\n"
                    + "      margin: 0;\n"
                    + "      padding: 0;\n"
                    + "    }\n"
                    + "    \n"
                    + "    table,\n"
                    + "    tr,\n"
                    + "    td {\n"
                    + "      vertical-align: top;\n"
                    + "      border-collapse: collapse;\n"
                    + "    }\n"
                    + "    \n"
                    + "    p {\n"
                    + "      margin: 0;\n"
                    + "    }\n"
                    + "    \n"
                    + "    .ie-container table,\n"
                    + "    .mso-container table {\n"
                    + "      table-layout: fixed;\n"
                    + "    }\n"
                    + "    \n"
                    + "    * {\n"
                    + "      line-height: inherit;\n"
                    + "    }\n"
                    + "    \n"
                    + "    a[x-apple-data-detectors='true'] {\n"
                    + "      color: inherit !important;\n"
                    + "      text-decoration: none !important;\n"
                    + "    }\n"
                    + "  </style>\n"
                    + "\n"
                    + "\n"
                    + "  <link href=\"https://fonts.googleapis.com/css?family=Cabin:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\">\n"
                    + "\n"
                    + "\n"
                    + "</head>\n"
                    + "\n"
                    + "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #f9f9f9;color: #000000\">\n"
                    + "  <table style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #f9f9f9;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\n"
                    + "    <tbody>\n"
                    + "      <tr style=\"vertical-align: top\">\n"
                    + "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">          \n"
                    + "          <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n"
                    + "            <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n"
                    + "              <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">               \n"
                    + "                <div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n"
                    + "                  <div style=\"width: 100% !important;\">\n"
                    + "                    <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">                   \n"
                 
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>\n"
                    + "              </div>\n"
                    + "            </div>\n"
                    + "          </div>\n"
                    + "\n"
                    + "\n"
                    + "\n"
                    + "          <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n"
                    + "            <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\n"
                    + "              <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">          \n"
                    + "                <div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n"
                    + "                  <div style=\"width: 100% !important;\">\n"
                    + "                    <div style=\"padding: 0px;border-top: 1px solid #CCC;border-left: 1px solid #CCC;border-right: 1px solid #CCC;border-bottom: 1px solid #CCC;\">                    \n"
                    + "                      <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n"
                    + "                        <tbody>\n"
                    + "                          <tr>\n"
                    + "                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:20px;font-family:'Cabin',sans-serif;\" align=\"left\">\n"
                    + "\n"
                    + "                              <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
                    + "                                <tr>\n"
                    + "                                  <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\n"
                    + "\n"
                    + "                                    <img align=\"center\" border=\"0\" src=\"https://static.vecteezy.com/system/resources/previews/001/886/209/non_2x/doctor-medical-cartoon-design-vector.jpg \" alt=\"Image\" title=\"Image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 81%;max-width: 453.6px;\"\n"
                    + "                                      width=\"453.6\" />\n"
                    + "\n"
                    + "                                  </td>\n"
                    + "                                </tr>\n"
                    + "                              </table>\n"
                    + "\n"
                    + "                            </td>\n"
                    + "                          </tr>\n"
                    + "                        </tbody>\n"
                    + "                      </table>\n"
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>\n"
                    + "              </div>\n"
                    + "            </div>\n"
                    + "          </div>\n"
                    + "\n"
                    + "\n"
                    + "\n"
                    + "          <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n"
                    + "            <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #003399;\">\n"
                    + "              <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">               \n"
                    + "                <div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n"
                    + "                  <div style=\"width: 100% !important;\">\n"
                    + "                    <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">               \n"
                    + "                      <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n"
                    + "                        <tbody>\n"
                    + "                          <tr>\n"
                    + "                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:40px 10px 10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n"
                    + "\n"
                    + "                              <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
                    + "                                <tr>\n"
                    + "                                  <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\n"
                    + "\n"
                    + "                                    <img align=\"center\" border=\"0\" src=\"https://cdn.templates.unlayer.com/assets/1597218650916-xxxxc.png\" alt=\"Image\" title=\"Image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 26%;max-width: 150.8px;\"\n"
                    + "                                      width=\"150.8\" />\n"
                    + "\n"
                    + "                                  </td>\n"
                    + "                                </tr>\n"
                    + "                              </table>\n"
                    + "\n"
                    + "                            </td>\n"
                    + "                          </tr>\n"
                    + "                        </tbody>\n"
                    + "                      </table>\n"
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>\n"
                    + "              </div>\n"
                    + "            </div>\n"
                    + "          </div>\n"
                    + "\n"
                    + "\n"
                    + "\n"
                    + "          <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n"
                    + "            <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\n"
                    + "              <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">                \n"
                    + "                <div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n"
                    + "                  <div style=\"width: 100% !important;\">\n"
                    + "                    <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">            \n"
                    + "                      <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n"
                    + "                        <tbody>\n"
                    + "                          <tr>\n"
                    + "                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:33px 55px;font-family:'Cabin',sans-serif;\" align=\"left\">\n"
                    + "\n"
                    + "                              <div style=\"line-height: 160%; text-align: center; word-wrap: break-word;\">\n"
                    
                    + mensaje
                    + "                              </div>\n"
                    + "\n"
                    + "                            </td>\n"
                    + "                          </tr>\n"
                    + "                        </tbody>\n"
                    + "                      </table>\n"
                    + "\n"
                    + "                      <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n"
                    + "                        <tbody>\n"
                    + "                          <tr>\n"
                    + "                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 55px;font-family:'Cabin',sans-serif;\" align=\"left\">\n"
                    + "\n"
                    + "                              <div style=\"line-height: 160%; text-align: center; word-wrap: break-word;\">\n"
                    + "                                <p style=\"font-size: 14px; line-height: 160%;\">Muchas gracias por su atenci&oacute;n</p>\n"
                    + "                              </div>\n"
                    + "\n"
                    + "                            </td>\n"
                    + "                          </tr>\n"
                    + "                        </tbody>\n"
                    + "                      </table>\n"
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>\n"
                    + "              </div>\n"
                    + "            </div>\n"
                    + "          </div>\n"
                    + "\n"
                    + "\n"
                    + "\n"
                    + "          <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n"
                    + "            <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #003399;\">\n"
                    + "              <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">            \n"
                    + "                <div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n"
                    + "                  <div style=\"width: 100% !important;\">\n"
                    + "                    <div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\n"
                    + "                  \n"
                    + "                      <table style=\"font-family:'Cabin',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n"
                    + "                        <tbody>\n"
                    + "                          <tr>\n"
                    + "                            <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Cabin',sans-serif;\" align=\"left\">\n"
                    + "\n"
                    + "                              <div style=\"color: #fafafa; line-height: 180%; text-align: center; word-wrap: break-word;\">\n"
                    + "                                <p style=\"font-size: 14px; line-height: 180%;\"><span style=\"font-size: 16px; line-height: 28.8px;\">Copyrights &copy; Deckxel derechos reservados</span></p>\n"
                    + "                              </div>\n"
                    + "\n"
                    + "                            </td>\n"
                    + "                          </tr>\n"
                    + "                        </tbody>\n"
                    + "                      </table>\n"
                    + "                    </div>\n"
                    + "                  </div>\n"
                    + "                </div>\n"
                    + "              </div>\n"
                    + "            </div>\n"
                    + "          </div>\n"
                    + "        </td>\n"
                    + "      </tr>\n"
                    + "    </tbody>\n"
                    + "  </table>\n"
                    + "</body>\n"
                    + "\n"
                    + "</html>";
            texto.setContent(HTMLENVIO, "text/html");

            MimeMultipart multiParte = new MimeMultipart();
            // inicio adjunto
            if (attachFiles != null && attachFiles.length > 0) {
                for (String filePath : attachFiles) {
                    MimeBodyPart attachPartDoc = new MimeBodyPart();
                    try {
                        if (!filePath.equals("")) {
                            attachPartDoc.attachFile(filePath);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    multiParte.addBodyPart(attachPartDoc);
                }
            }
            m.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(address));
            multiParte.addBodyPart(texto);

//            m.setRecipients(Message.RecipientType.TO, addresTto);
//            m.setRecipients(Message.RecipientType.BCC, from);
            m.setSubject(asunto);
            m.setSentDate(new java.util.Date());
//             m.setContent(dirDatos, "text/plain");
            m.setContent(multiParte);

            Transport t = session.getTransport(protocol);
//             t.connect();
            t.connect(host, usuarioSmpt, password);
            t.send(m);
            t.close();
            return true;
        } catch (javax.mail.MessagingException e) {
            System.out.println("error" + e);
            e.printStackTrace();

            return false;
        }
    }
}
