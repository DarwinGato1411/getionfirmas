/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author PC
 */
public class HelperPersistencia {

    public static EntityManager getEMF() {
        try {
            EntityManager emf = (EntityManager) Persistence.createEntityManagerFactory("GestionFirmasPU").createEntityManager();
            return emf;
        } catch (Exception e) {
            System.out.println("ERROR PERSISTENCIA " + e.getLocalizedMessage());
        }
        return null;
    }
}
