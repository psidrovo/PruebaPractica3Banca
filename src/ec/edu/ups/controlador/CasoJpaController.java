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
import ec.edu.ups.modelo.Banco;
import ec.edu.ups.modelo.Caso;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Paul Idrovo
 */
public class CasoJpaController implements Serializable {

    public CasoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Caso caso) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Banco bancoId = caso.getBancoId();
            if (bancoId != null) {
                bancoId = em.getReference(bancoId.getClass(), bancoId.getBancoId());
                caso.setBancoId(bancoId);
            }
            em.persist(caso);
            if (bancoId != null) {
                bancoId.getCasoCollection().add(caso);
                bancoId = em.merge(bancoId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Caso caso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Caso persistentCaso = em.find(Caso.class, caso.getCodigo());
            Banco bancoIdOld = persistentCaso.getBancoId();
            Banco bancoIdNew = caso.getBancoId();
            if (bancoIdNew != null) {
                bancoIdNew = em.getReference(bancoIdNew.getClass(), bancoIdNew.getBancoId());
                caso.setBancoId(bancoIdNew);
            }
            caso = em.merge(caso);
            if (bancoIdOld != null && !bancoIdOld.equals(bancoIdNew)) {
                bancoIdOld.getCasoCollection().remove(caso);
                bancoIdOld = em.merge(bancoIdOld);
            }
            if (bancoIdNew != null && !bancoIdNew.equals(bancoIdOld)) {
                bancoIdNew.getCasoCollection().add(caso);
                bancoIdNew = em.merge(bancoIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = caso.getCodigo();
                if (findCaso(id) == null) {
                    throw new NonexistentEntityException("The caso with id " + id + " no longer exists.");
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
            Caso caso;
            try {
                caso = em.getReference(Caso.class, id);
                caso.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The caso with id " + id + " no longer exists.", enfe);
            }
            Banco bancoId = caso.getBancoId();
            if (bancoId != null) {
                bancoId.getCasoCollection().remove(caso);
                bancoId = em.merge(bancoId);
            }
            em.remove(caso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Caso> findCasoEntities() {
        return findCasoEntities(true, -1, -1);
    }

    public List<Caso> findCasoEntities(int maxResults, int firstResult) {
        return findCasoEntities(false, maxResults, firstResult);
    }

    private List<Caso> findCasoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Caso.class));
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

    public Caso findCaso(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Caso.class, id);
        } finally {
            em.close();
        }
    }

    public int getCasoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Caso> rt = cq.from(Caso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
