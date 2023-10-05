/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.consumirws;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author Darwin
 */
public class ServiciosRest {

    private String url = "http://localhost:8443/api/procesar-firma-empresa";

    public RespuestaProceso obtenerFirmaEmpresa(RequestApiEmpresa param, String tipoEmision) {

        System.out.println("TIPO EMIION " + tipoEmision);
        if (tipoEmision.equals("PN")) {
            //PERSONA NATURAL
            System.out.println("INGRESA PN");
            url = "http://localhost:8443/api/procesar-persona-natural";
        } else if (tipoEmision.equals("ME")) {
            System.out.println("INGRESA ME");
            url = "http://localhost:8443/api/procesar-firma-empresa";
        } else if (tipoEmision.equals("RLE")) {
            System.out.println("INGRESA PN");
            url = "http://localhost:8443/api/procesar-persona-juridica";
        } else {
            System.out.println("NO INGRESA");
            return new RespuestaProceso("500", "ERRO EN EL API");
        }

        try {
//

            OkHttpClient clientw = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"idSolicitud\":" + param.getIdSolicitud() + ",\r\n    \"idUsuario\":" + param.getIdUsuario() + ",\r\n    \"clave\":\""
                    + param.getClave() + "\"\r\n\r\n}");

            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = clientw.newCall(request).execute();
            String valorResp = response.body().string();
//            System.out.println("RESPUESTA JSON WS " + valorResp);
            Gson gson = new Gson(); // Or use new GsonBuilder().create();
            RespuestaProceso target2 = gson.fromJson(valorResp, RespuestaProceso.class);
//            System.out.println("path WS "+target2.getObservacion());
            //JSONObject outlineArray = new JSONObject(contenido);
//            return new RespuestaProceso(String.valueOf(200), valorResp);
            return target2;

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ServiciosRest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR " + ex.getMessage());
            return new RespuestaProceso(String.valueOf(ex.getMessage()), "ERRO EN EL API");
        } catch (IOException ex) {
            Logger.getLogger(ServiciosRest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR " + ex.getMessage());
            return new RespuestaProceso(String.valueOf(ex.getMessage()), "ERRO EN EL API");
        }

    }

    public String revocarFirma(RequestApiEmpresa param) {

        url = "http://localhost:8443/api/revocar-firma";

        try {
//
            OkHttpClient clientw = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"idSolicitud\":" + param.getIdSolicitud() + ",\r\n    \"idUsuario\":" + param.getIdUsuario() + ",\r\n    \"clave\":\""
                    + param.getClave() + "\"\r\n\r\n}");

            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = clientw.newCall(request).execute();
            String valorResp = response.body().string();
//         
            System.out.println(valorResp);
//            Gson gson = new Gson(); // Or use new GsonBuilder().create();
//            RespuestaProceso target2 = gson.fromJson(valorResp, RespuestaProceso.class);

            return valorResp;

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ServiciosRest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("1 ERROR " + ex.getMessage());
            return "1 ERROR EN EL API";
        } catch (IOException ex) {
            Logger.getLogger(ServiciosRest.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("2 ERROR " + ex.getMessage());
            return "2 ERROR EN EL API";
        }
    }
}
