/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.TipoFirma;
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
public class ServicioTipoFirma {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(TipoFirma tipoFirma) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(tipoFirma);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar tipoFirma " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(TipoFirma tipoFirma) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(tipoFirma));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  tipoFirma " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(TipoFirma tipoFirma) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(tipoFirma);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar tipoFirma " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<TipoFirma> finAll() {
        List<TipoFirma> listaTipoFirmas = new ArrayList<TipoFirma>();
        try {
            System.out.println("Entra a consultar tipoFirmas");
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM TipoFirma u  ORDER BY u.tipDescripcion ASC");
            listaTipoFirmas = (List<TipoFirma>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error tipoFirmas finAll " + e.getMessage());
        } finally {
            em.close();
        }

        return listaTipoFirmas;
    }

   
}
