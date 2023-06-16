/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author gato
 */
public class ServicioGeneral {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void eliminarExamenesRecetas(Integer idVisitaMedica) {
        try {

            em = HelperPersistencia.getEMF();

            em.getTransaction().begin();

            StoredProcedureQuery queryStore = em.createStoredProcedureQuery("eliminar_examen_receta");
            queryStore.registerStoredProcedureParameter("id_visita_param", Integer.class, ParameterMode.IN);

            queryStore.setParameter("id_visita_param", idVisitaMedica);
            queryStore.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            System.out.println("Error generarEvaluacion " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public String generarAgenda(Integer idUsuario, Integer numeroDia, Date fecha) {
        String mensaje = "";
        try {

            em = HelperPersistencia.getEMF();

            em.getTransaction().begin();

            StoredProcedureQuery queryStore = em.createStoredProcedureQuery("generar_agenda");
            queryStore.registerStoredProcedureParameter("id_usuariopar", Integer.class, ParameterMode.IN);
            queryStore.registerStoredProcedureParameter("hor_numero_diapar", Integer.class, ParameterMode.IN);
            queryStore.registerStoredProcedureParameter("fechapar", Date.class, ParameterMode.IN);
//            queryStore.registerStoredProcedureParameter("fechapar", Date.class, ParameterMode.OUT);

            queryStore.setParameter("id_usuariopar", idUsuario);
            queryStore.setParameter("hor_numero_diapar", numeroDia);
            queryStore.setParameter("fechapar", fecha);
            queryStore.execute();
            Object[] value = (Object[]) queryStore.getSingleResult();
//            String valor = (String)queryStore.getOutputParameterValue(1);
            System.out.println("retorno " + String.valueOf(value[0]));
            mensaje = String.valueOf(value[0]);
            em.getTransaction().commit();
        } catch (Exception e) {
            mensaje = e.getMessage();
            if (em != null) {
                em.getTransaction().rollback();
            }
            System.out.println("Error generarEvaluacion " + e.getMessage());
        } finally {
            em.close();
        }
        return mensaje;
    }
    public String generarHorario(Integer idUsuario) {
        String mensaje = "";
        try {

            em = HelperPersistencia.getEMF();

            em.getTransaction().begin();

            StoredProcedureQuery queryStore = em.createStoredProcedureQuery("generar_horario_doctor");
            queryStore.registerStoredProcedureParameter("id_usuariopar", Integer.class, ParameterMode.IN);
           

            queryStore.setParameter("id_usuariopar", idUsuario);
           
            queryStore.execute();
            Object[] value = (Object[]) queryStore.getSingleResult();
//            String valor = (String)queryStore.getOutputParameterValue(1);
            System.out.println("retorno " + String.valueOf(value[0]));
            mensaje = String.valueOf(value[0]);
            em.getTransaction().commit();
        } catch (Exception e) {
            mensaje = e.getMessage();
            if (em != null) {
                em.getTransaction().rollback();
            }
            System.out.println("Error generarEvaluacion " + e.getMessage());
        } finally {
            em.close();
        }
        return mensaje;
    }

}
