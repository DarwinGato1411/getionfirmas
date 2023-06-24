/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.DetalleTipoFirma;
import com.ec.entidad.TipoFirma;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioDetalleTipoFirma {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(DetalleTipoFirma detalleTipoFirma) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(detalleTipoFirma);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar detalleTipoFirma " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(DetalleTipoFirma detalleTipoFirma) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(detalleTipoFirma));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  detalleTipoFirma " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(DetalleTipoFirma detalleTipoFirma) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(detalleTipoFirma);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar detalleTipoFirma " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<DetalleTipoFirma> finAll() {
        List<DetalleTipoFirma> listaDetalleTipoFirmas = new ArrayList<DetalleTipoFirma>();
        try {
            System.out.println("Entra a consultar detalleTipoFirmas");
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM DetalleTipoFirma u  ORDER BY u.tipDescripcion ASC");
            listaDetalleTipoFirmas = (List<DetalleTipoFirma>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error detalleTipoFirmas finAll " + e.getMessage());
        } finally {
            em.close();
        }

        return listaDetalleTipoFirmas;
    }
  
    public List<DetalleTipoFirma> findByTipoFirma(TipoFirma tipoFirma) {
        List<DetalleTipoFirma> listaDetalleTipoFirmas = new ArrayList<DetalleTipoFirma>();
        try {
//            System.out.println("Entra a consultar detalleTipoFirmas");
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM DetalleTipoFirma u WHERE u.idTipoFirma=:idTipoFirma  ORDER BY u.detDescripcion ASC");
            query.setParameter("idTipoFirma", tipoFirma);
            listaDetalleTipoFirmas = (List<DetalleTipoFirma>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error detalleTipoFirmas finAll " + e.getMessage());
        } finally {
            em.close();
        }

        return listaDetalleTipoFirmas;
    }

   
}
