/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.consumirws;

import java.io.IOException;
import static javax.ws.rs.client.Entity.json;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static org.converter.JavaObjectsConverter.Format.JSON;

/**
 *
 * @author Darwin
 */
public class ServiciosRest {

    private String url = "http://localhost:8443/api/procesar-firma-empresa";

    public Boolean obtenerFirmaEmpresa(RequestApiEmpresa param, String tipoEmision) {
        if (tipoEmision.equals("PN")) {
            //PERSONA NATURAL
            url = "http://localhost:8443/api/procesar-persona-natural";
        } else if (tipoEmision.equals("ME")) {
            url = "http://localhost:8443/api/procesar-firma-empresa";
        } else if (tipoEmision.equals("RLE")) {
            url = "http://localhost:8443/api/procesar-persona-juridica";
        } else {
            return false;
        }
        try {

            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(JSON, "{\n"
                        + "  \"idSolicitud\": " + param.getIdSolicitud() + ",\n"
                        + "  \"idUsuario\": " + param.getIdUsuario() + "\n"
                        + "}");
//                RequestBody formBody = new FormBody.Builder()
//                            .add("message", "Your message")
//                            .build();
            Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            System.out.println("responseBody " + responseBody);
//                RespuestaProceso respuestaProceso = response.body();
            // Do something with the response.
//                if (respuestaProceso.getCodigo().contains("200")) {
//                    return true;
//                } else {
//                    return false;
//                }

            return true;
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            return false;
        }

    }
}
