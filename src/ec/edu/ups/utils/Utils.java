/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ups.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Paul Idrovo
 */
public class Utils {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Prueba3_IdrovoPU");
    
    public static EntityManagerFactory getEntityManagerFactory(){
        return emf;
    }
    public  static EntityManager getEntityManager(){
        return emf.createEntityManager();
    }
}
