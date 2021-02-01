/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ups.controlador;

import ec.edu.ups.controlador.exceptions.IllegalOrphanException;
import ec.edu.ups.controlador.exceptions.NonexistentEntityException;
import ec.edu.ups.controlador.exceptions.PreexistingEntityException;
import ec.edu.ups.modelo.Banco;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ec.edu.ups.modelo.Caso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Paul Idrovo
 */
public class BancoJpaController implements Serializable {

    public BancoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Banco banco) throws PreexistingEntityException, Exception {
        if (banco.getCasoCollection() == null) {
            banco.setCasoCollection(new ArrayList<Caso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Caso> attachedCasoCollection = new ArrayList<Caso>();
            for (Caso casoCollectionCasoToAttach : banco.getCasoCollection()) {
                casoCollectionCasoToAttach = em.getReference(casoCollectionCasoToAttach.getClass(), casoCollectionCasoToAttach.getCodigo());
                attachedCasoCollection.add(casoCollectionCasoToAttach);
            }
            banco.setCasoCollection(attachedCasoCollection);
            em.persist(banco);
            for (Caso casoCollectionCaso : banco.getCasoCollection()) {
                Banco oldBancoIdOfCasoCollectionCaso = casoCollectionCaso.getBancoId();
                casoCollectionCaso.setBancoId(banco);
                casoCollectionCaso = em.merge(casoCollectionCaso);
                if (oldBancoIdOfCasoCollectionCaso != null) {
                    oldBancoIdOfCasoCollectionCaso.getCasoCollection().remove(casoCollectionCaso);
                    oldBancoIdOfCasoCollectionCaso = em.merge(oldBancoIdOfCasoCollectionCaso);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBanco(banco.getBancoId()) != null) {
                throw new PreexistingEntityException("Banco " + banco + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Banco banco) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Banco persistentBanco = em.find(Banco.class, banco.getBancoId());
            Collection<Caso> casoCollectionOld = persistentBanco.getCasoCollection();
            Collection<Caso> casoCollectionNew = banco.getCasoCollection();
            List<String> illegalOrphanMessages = null;
            for (Caso casoCollectionOldCaso : casoCollectionOld) {
                if (!casoCollectionNew.contains(casoCollectionOldCaso)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Caso " + casoCollectionOldCaso + " since its bancoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Caso> attachedCasoCollectionNew = new ArrayList<Caso>();
            for (Caso casoCollectionNewCasoToAttach : casoCollectionNew) {
                casoCollectionNewCasoToAttach = em.getReference(casoCollectionNewCasoToAttach.getClass(), casoCollectionNewCasoToAttach.getCodigo());
                attachedCasoCollectionNew.add(casoCollectionNewCasoToAttach);
            }
            casoCollectionNew = attachedCasoCollectionNew;
            banco.setCasoCollection(casoCollectionNew);
            banco = em.merge(banco);
            for (Caso casoCollectionNewCaso : casoCollectionNew) {
                if (!casoCollectionOld.contains(casoCollectionNewCaso)) {
                    Banco oldBancoIdOfCasoCollectionNewCaso = casoCollectionNewCaso.getBancoId();
                    casoCollectionNewCaso.setBancoId(banco);
                    casoCollectionNewCaso = em.merge(casoCollectionNewCaso);
                    if (oldBancoIdOfCasoCollectionNewCaso != null && !oldBancoIdOfCasoCollectionNewCaso.equals(banco)) {
                        oldBancoIdOfCasoCollectionNewCaso.getCasoCollection().remove(casoCollectionNewCaso);
                        oldBancoIdOfCasoCollectionNewCaso = em.merge(oldBancoIdOfCasoCollectionNewCaso);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = banco.getBancoId();
                if (findBanco(id) == null) {
                    throw new NonexistentEntityException("The banco with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Banco banco;
            try {
                banco = em.getReference(Banco.class, id);
                banco.getBancoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The banco with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Caso> casoCollectionOrphanCheck = banco.getCasoCollection();
            for (Caso casoCollectionOrphanCheckCaso : casoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Banco (" + banco + ") cannot be destroyed since the Caso " + casoCollectionOrphanCheckCaso + " in its casoCollection field has a non-nullable bancoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(banco);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Banco> findBancoEntities() {
        return findBancoEntities(true, -1, -1);
    }

    public List<Banco> findBancoEntities(int maxResults, int firstResult) {
        return findBancoEntities(false, maxResults, firstResult);
    }

    private List<Banco> findBancoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Banco.class));
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

    public Banco findBanco(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Banco.class, id);
        } finally {
            em.close();
        }
    }

    public int getBancoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Banco> rt = cq.from(Banco.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
