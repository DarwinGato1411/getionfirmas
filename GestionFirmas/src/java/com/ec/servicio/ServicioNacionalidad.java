/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.Nacionalidad;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioNacionalidad {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(Nacionalidad nacionalidad) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(nacionalidad);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar nacionalidad " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(Nacionalidad nacionalidad) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(nacionalidad));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  nacionalidad " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(Nacionalidad nacionalidad) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(nacionalidad);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar nacionalidad " + e.getMessage());
        } finally {
            em.close();
        }

    }

   
    public List<Nacionalidad> finAll() {
        List<Nacionalidad> listaNacionalidads = new ArrayList<Nacionalidad>();
        try {
            System.out.println("Entra a consultar nacionalidads");
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM Nacionalidad u  ORDER BY u.nacNombre ASC");
            listaNacionalidads = (List<Nacionalidad>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error nacionalidads finAll " + e.getMessage());
        } finally {
            em.close();
        }

        return listaNacionalidads;
    }
}
