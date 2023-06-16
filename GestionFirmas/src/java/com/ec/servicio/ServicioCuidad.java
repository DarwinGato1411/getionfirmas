/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.Ciudad;
import com.ec.entidad.EstadoProceso;
import com.ec.entidad.Provincia;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioCuidad {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(Ciudad ciudad) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(ciudad);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar ciudad " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(Ciudad ciudad) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(ciudad));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  ciudad " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(Ciudad ciudad) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(ciudad);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar ciudad " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<Ciudad> finAll() {
        List<Ciudad> listaCiudads = new ArrayList<Ciudad>();
        try {
            System.out.println("Entra a consultar ciudads");
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM Ciudad u  ORDER BY u.provNombre ASC");
            listaCiudads = (List<Ciudad>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error ciudads finAll " + e.getMessage());
        } finally {
            em.close();
        }

        return listaCiudads;
    }

    public List<Ciudad> findByProvincia(Provincia provincia) {

        List<Ciudad> listado = new ArrayList<Ciudad>();
        try {
            //Connection connection = em.unwrap(Connection.class);

            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM Ciudad u WHERE u.idProvincia = :idProvincia");
            query.setParameter("idProvincia", provincia);
            listado = (List<Ciudad>) query.getResultList();

            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en la consulta ciudads  findByProvincia  " + e.getMessage());
        } finally {
            em.close();
        }

        return listado;
    }
}
