/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.EstadoFirma;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioEstadoFirma {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(EstadoFirma estadoFima) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(estadoFima);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar estadoFima " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(EstadoFirma estadoFima) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(estadoFima));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  estadoFima " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(EstadoFirma estadoFima) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(estadoFima);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar estadoFima " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public EstadoFirma findBySigla(String sigla) {

        List<EstadoFirma> listaClientes = new ArrayList<EstadoFirma>();
        EstadoFirma estadoFimaObtenido = new EstadoFirma();
        try {
            //Connection connection = em.unwrap(Connection.class);

            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM EstadoFirma u WHERE u.estSigla = :estSigla");
            query.setParameter("estSigla", sigla);
            listaClientes = (List<EstadoFirma>) query.getResultList();
            if (listaClientes.size() > 0) {
                for (EstadoFirma estadoFima : listaClientes) {
                    estadoFimaObtenido = estadoFima;
                }
            } else {
                estadoFimaObtenido = null;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta estadoFima  FindEstadoFirmaPorNombre  " + e.getMessage());
        } finally {
            em.close();
        }

        return estadoFimaObtenido;
    }

    public List<EstadoFirma> finAll(String nombre) {
        List<EstadoFirma> listaEstadoFirmas = new ArrayList<EstadoFirma>();
        try {
            System.out.println("Entra a consultar estadoFimas");
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM EstadoFirma u ");
            listaEstadoFirmas = (List<EstadoFirma>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error estadoFimas finAll " + e.getMessage());
        } finally {
            em.close();
        }

        return listaEstadoFirmas;
    }
}
