/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador.consumirws;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
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

            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(JSON, "{\n"
                        + "  \"idSolicitud\": " + param.getIdSolicitud() + ",\n"
                        + "  \"idUsuario\": " + param.getIdUsuario() + ",\n"
                        + "  \"clave\": " + param.getClave() + "\n"
                        + "}");
//                RequestBody formBody = new FormBody.Builder()
//                            .add("message", "Your message")
//                            .build();
            Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

            Response response = client.newCall(request).execute();
            String valorResp=response.body().string();
            Gson g = new Gson();
                        System.out.println("response " + valorResp);
            RespuestaProceso respuesta = g.fromJson(valorResp, RespuestaProceso.class);
//            System.out.println("response " + response.body().toString());
            System.out.println("respuesta " + respuesta.getObservacion());
            int statusCode = response.code();

            if (statusCode == 200) {
                if (respuesta.getMensaje().toUpperCase().contains("ERROR")) {
                    return new RespuestaProceso(String.valueOf(statusCode), "ERRO EN EL API");
                } else {
                    return respuesta;
                }

            } else {
                return new RespuestaProceso(String.valueOf(statusCode), "ERRO EN EL API");
            }

        } catch (JsonSyntaxException | IOException e) {
            System.out.println("ERROR " + e.getMessage());
            return new RespuestaProceso(String.valueOf(e.getMessage()), "ERRO EN EL API");
        }

    }
}
