/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ups.controlador;

import ec.edu.ups.controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ec.edu.ups.modelo.Persona;
import ec.edu.ups.modelo.Vivienda;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Paul Idrovo
 */
public class ViviendaJpaController implements Serializable {

    public ViviendaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vivienda vivienda) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona personaCedula = vivienda.getPersonaCedula();
            if (personaCedula != null) {
                personaCedula = em.getReference(personaCedula.getClass(), personaCedula.getCedula());
                vivienda.setPersonaCedula(personaCedula);
            }
            em.persist(vivienda);
            if (personaCedula != null) {
                personaCedula.getViviendaCollection().add(vivienda);
                personaCedula = em.merge(personaCedula);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vivienda vivienda) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vivienda persistentVivienda = em.find(Vivienda.class, vivienda.getCodigoCatastral());
            Persona personaCedulaOld = persistentVivienda.getPersonaCedula();
            Persona personaCedulaNew = vivienda.getPersonaCedula();
            if (personaCedulaNew != null) {
                personaCedulaNew = em.getReference(personaCedulaNew.getClass(), personaCedulaNew.getCedula());
                vivienda.setPersonaCedula(personaCedulaNew);
            }
            vivienda = em.merge(vivienda);
            if (personaCedulaOld != null && !personaCedulaOld.equals(personaCedulaNew)) {
                personaCedulaOld.getViviendaCollection().remove(vivienda);
                personaCedulaOld = em.merge(personaCedulaOld);
            }
            if (personaCedulaNew != null && !personaCedulaNew.equals(personaCedulaOld)) {
                personaCedulaNew.getViviendaCollection().add(vivienda);
                personaCedulaNew = em.merge(personaCedulaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vivienda.getCodigoCatastral();
                if (findVivienda(id) == null) {
                    throw new NonexistentEntityException("The vivienda with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vivienda vivienda;
            try {
                vivienda = em.getReference(Vivienda.class, id);
                vivienda.getCodigoCatastral();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vivienda with id " + id + " no longer exists.", enfe);
            }
            Persona personaCedula = vivienda.getPersonaCedula();
            if (personaCedula != null) {
                personaCedula.getViviendaCollection().remove(vivienda);
                personaCedula = em.merge(personaCedula);
            }
            em.remove(vivienda);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Vivienda> findViviendaEntities() {
        return findViviendaEntities(true, -1, -1);
    }

    public List<Vivienda> findViviendaEntities(int maxResults, int firstResult) {
        return findViviendaEntities(false, maxResults, firstResult);
    }

    private List<Vivienda> findViviendaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vivienda.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Vivienda findVivienda(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vivienda.class, id);
        } finally {
            em.close();
        }
    }

    public int getViviendaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vivienda> rt = cq.from(Vivienda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
