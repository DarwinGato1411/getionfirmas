/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.EstadoFirma;
import com.ec.entidad.EstadoProceso;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioEstadoProceso {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(EstadoProceso estadoProceso) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(estadoProceso);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar estadoProceso " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(EstadoProceso estadoProceso) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(estadoProceso));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  estadoProceso " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(EstadoProceso estadoProceso) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(estadoProceso);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar estadoProceso " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public EstadoProceso findBySigla(String sigla) {

        List<EstadoProceso> listaClientes = new ArrayList<EstadoProceso>();
        EstadoProceso estadoProcesoObtenido = new EstadoProceso();
        try {
            //Connection connection = em.unwrap(Connection.class);

            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM EstadoProceso u WHERE u.estSigla = :estSigla");
            query.setParameter("estSigla", sigla);
            listaClientes = (List<EstadoProceso>) query.getResultList();
            if (listaClientes.size() > 0) {
                for (EstadoProceso estadoProceso : listaClientes) {
                    estadoProcesoObtenido = estadoProceso;
                }
            } else {
                estadoProcesoObtenido = null;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta estadoProceso  FindEstadoProcesoPorNombre  " + e.getMessage());
        } finally {
            em.close();
        }

        return estadoProcesoObtenido;
    }

    public List<EstadoProceso> finAll() {
        List<EstadoProceso> listaEstadoProcesos = new ArrayList<EstadoProceso>();
        try {
            System.out.println("Entra a consultar estadoProcesos");
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM EstadoProceso u ");
            listaEstadoProcesos = (List<EstadoProceso>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error estadoProcesos finAll " + e.getMessage());
        } finally {
            em.close();
        }

        return listaEstadoProcesos;
    }
    
    public List<EstadoFirma> finAllFirmas() {
        List<EstadoFirma> listaEstadoProcesos = new ArrayList<EstadoFirma>();
        try {
            System.out.println("Entra a consultar EstadoFirma");
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM EstadoFirma u ");
            listaEstadoProcesos = (List<EstadoFirma>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error estadoProcesos finAll " + e.getMessage());
        } finally {
            em.close();
        }

        return listaEstadoProcesos;
    }
}
