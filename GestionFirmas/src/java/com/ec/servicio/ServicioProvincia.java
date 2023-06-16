/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.Provincia;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioProvincia {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(Provincia provincia) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(provincia);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar provincia " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(Provincia provincia) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(provincia));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  provincia " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(Provincia provincia) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(provincia);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar provincia " + e.getMessage());
        } finally {
            em.close();
        }

    }

   
    public List<Provincia> finAll() {
        List<Provincia> listaProvincias = new ArrayList<Provincia>();
        try {
            System.out.println("Entra a consultar provincias");
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM Provincia u  ORDER BY u.provNombre ASC");
            listaProvincias = (List<Provincia>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error provincias finAll " + e.getMessage());
        } finally {
            em.close();
        }

        return listaProvincias;
    }
}
