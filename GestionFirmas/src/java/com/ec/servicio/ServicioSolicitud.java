/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.Solicitud;
import com.ec.entidad.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioSolicitud {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(Solicitud solicitud) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(solicitud);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar solicitud " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(Solicitud solicitud) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(solicitud));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  solicitu " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(Solicitud solicitud) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(solicitud);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar solicitud " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public Solicitud findSolicitudPorNombre(String nombre) {

        List<Solicitud> listaClientes = new ArrayList<Solicitud>();
        Solicitud solicitudObtenido = new Solicitud();
        try {
            //Connection connection = em.unwrap(Connection.class);

            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT u FROM Solicitud u WHERE u.solNombre = :solNombre");
            query.setParameter("solNombre", nombre);
            listaClientes = (List<Solicitud>) query.getResultList();
            if (listaClientes.size() > 0) {
                for (Solicitud solicitud : listaClientes) {
                    solicitudObtenido = solicitud;
                }
            } else {
                solicitudObtenido = null;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta solicitud  FindSolicitudPorNombre  " + e);
        } finally {
            em.close();
        }

        return solicitudObtenido;
    }

    public List<Solicitud> findLikeSolicitud(String nombre, Usuario usuario) {
        List<Solicitud> listaSolicituds = new ArrayList<Solicitud>();
        try {

            String SQL = "SELECT u FROM Solicitud u WHERE u.solNombre like :solNombre ";
            String WHERE = " AND u.idUsuario=:idUsuario";
            String ORDERBY = " ORDER BY u.solNombre ASC";
            System.out.println("Entra a consultar solicituds");
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();

            if (usuario.getUsuNivel() == 1) {
                SQL = SQL + WHERE + ORDERBY;
            } else {
                SQL = SQL + ORDERBY;
            }
            Query query = em.createQuery(SQL);
            query.setParameter("solNombre", "%" + nombre + "%");

            if (usuario.getUsuNivel() == 1) {
                query.setParameter("idUsuario", usuario);
            }
            listaSolicituds = (List<Solicitud>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error solicituds finAll " + e.getMessage());
        } finally {
            em.close();
        }

        return listaSolicituds;
    }
}
