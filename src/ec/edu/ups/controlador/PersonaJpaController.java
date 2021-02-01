/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.ups.controlador;

import ec.edu.ups.controlador.exceptions.IllegalOrphanException;
import ec.edu.ups.controlador.exceptions.NonexistentEntityException;
import ec.edu.ups.controlador.exceptions.PreexistingEntityException;
import ec.edu.ups.modelo.Persona;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ec.edu.ups.modelo.Vivienda;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Paul Idrovo
 */
public class PersonaJpaController implements Serializable {

    public PersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Persona persona) throws PreexistingEntityException, Exception {
        if (persona.getViviendaCollection() == null) {
            persona.setViviendaCollection(new ArrayList<Vivienda>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Vivienda> attachedViviendaCollection = new ArrayList<Vivienda>();
            for (Vivienda viviendaCollectionViviendaToAttach : persona.getViviendaCollection()) {
                viviendaCollectionViviendaToAttach = em.getReference(viviendaCollectionViviendaToAttach.getClass(), viviendaCollectionViviendaToAttach.getCodigoCatastral());
                attachedViviendaCollection.add(viviendaCollectionViviendaToAttach);
            }
            persona.setViviendaCollection(attachedViviendaCollection);
            em.persist(persona);
            for (Vivienda viviendaCollectionVivienda : persona.getViviendaCollection()) {
                Persona oldPersonaCedulaOfViviendaCollectionVivienda = viviendaCollectionVivienda.getPersonaCedula();
                viviendaCollectionVivienda.setPersonaCedula(persona);
                viviendaCollectionVivienda = em.merge(viviendaCollectionVivienda);
                if (oldPersonaCedulaOfViviendaCollectionVivienda != null) {
                    oldPersonaCedulaOfViviendaCollectionVivienda.getViviendaCollection().remove(viviendaCollectionVivienda);
                    oldPersonaCedulaOfViviendaCollectionVivienda = em.merge(oldPersonaCedulaOfViviendaCollectionVivienda);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersona(persona.getCedula()) != null) {
                throw new PreexistingEntityException("Persona " + persona + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persistentPersona = em.find(Persona.class, persona.getCedula());
            Collection<Vivienda> viviendaCollectionOld = persistentPersona.getViviendaCollection();
            Collection<Vivienda> viviendaCollectionNew = persona.getViviendaCollection();
            List<String> illegalOrphanMessages = null;
            for (Vivienda viviendaCollectionOldVivienda : viviendaCollectionOld) {
                if (!viviendaCollectionNew.contains(viviendaCollectionOldVivienda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Vivienda " + viviendaCollectionOldVivienda + " since its personaCedula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Vivienda> attachedViviendaCollectionNew = new ArrayList<Vivienda>();
            for (Vivienda viviendaCollectionNewViviendaToAttach : viviendaCollectionNew) {
                viviendaCollectionNewViviendaToAttach = em.getReference(viviendaCollectionNewViviendaToAttach.getClass(), viviendaCollectionNewViviendaToAttach.getCodigoCatastral());
                attachedViviendaCollectionNew.add(viviendaCollectionNewViviendaToAttach);
            }
            viviendaCollectionNew = attachedViviendaCollectionNew;
            persona.setViviendaCollection(viviendaCollectionNew);
            persona = em.merge(persona);
            for (Vivienda viviendaCollectionNewVivienda : viviendaCollectionNew) {
                if (!viviendaCollectionOld.contains(viviendaCollectionNewVivienda)) {
                    Persona oldPersonaCedulaOfViviendaCollectionNewVivienda = viviendaCollectionNewVivienda.getPersonaCedula();
                    viviendaCollectionNewVivienda.setPersonaCedula(persona);
                    viviendaCollectionNewVivienda = em.merge(viviendaCollectionNewVivienda);
                    if (oldPersonaCedulaOfViviendaCollectionNewVivienda != null && !oldPersonaCedulaOfViviendaCollectionNewVivienda.equals(persona)) {
                        oldPersonaCedulaOfViviendaCollectionNewVivienda.getViviendaCollection().remove(viviendaCollectionNewVivienda);
                        oldPersonaCedulaOfViviendaCollectionNewVivienda = em.merge(oldPersonaCedulaOfViviendaCollectionNewVivienda);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = persona.getCedula();
                if (findPersona(id) == null) {
                    throw new NonexistentEntityException("The persona with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The persona with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Vivienda> viviendaCollectionOrphanCheck = persona.getViviendaCollection();
            for (Vivienda viviendaCollectionOrphanCheckVivienda : viviendaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Vivienda " + viviendaCollectionOrphanCheckVivienda + " in its viviendaCollection field has a non-nullable personaCedula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(persona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Persona> findPersonaEntities() {
        return findPersonaEntities(true, -1, -1);
    }

    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return findPersonaEntities(false, maxResults, firstResult);
    }

    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Persona.class));
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

    public Persona findPersona(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Persona> rt = cq.from(Persona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
