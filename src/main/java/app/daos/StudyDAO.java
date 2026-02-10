package app.daos;

import app.entities.Study;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class StudyDAO implements IDAO<Study, Integer>{

    private final EntityManagerFactory emf;

    public StudyDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Study create(Study study) {
        if (study == null) {
            throw new ApiException(400, "Study is required");
        }
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            try {
                em.persist(study);
                em.getTransaction().commit();
            } catch (PersistenceException e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw new ApiException(500, "Create study failed: " + e.getMessage());
            } catch (RuntimeException e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw e;
            }
        }
        return study;
    }

    @Override
    public Study getById(Integer id) {
        if (id == null) {
            throw new ApiException(400, "Study id is required");
        }
        try (EntityManager em = emf.createEntityManager()) {
            try {
                Study study = em.find(Study.class, id);
                if (study != null) {
                    return study;
                }
                throw new ApiException(404, "Study not found");
            } catch (PersistenceException e) {
                throw new ApiException(500, "Get study failed: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Study> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            try {
                TypedQuery<Study> query = em.createQuery("SELECT s FROM Study s", Study.class);
                return query.getResultList();
            } catch (PersistenceException e) {
                throw new ApiException(500, "Get studies failed: " + e.getMessage());
            }
        }
    }

    @Override
    public Study update(Study study) {
        if (study == null || study.getId() == null) {
            throw new ApiException(400, "Study id is required");
        }
        Study updated;
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            try {
                Study existing = em.find(Study.class, study.getId());
                if (existing == null) {
                    throw new ApiException(404, "Study not found");
                }
                updated = em.merge(study);
                em.getTransaction().commit();
            } catch (PersistenceException e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw new ApiException(500, "Update study failed: " + e.getMessage());
            } catch (RuntimeException e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw e;
            }
        }
        return updated;
    }

    @Override
    public boolean delete(Integer id) {
        if (id == null) {
            throw new ApiException(400, "Study id is required");
        }
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            try {
                Study studyToRemove = em.find(Study.class, id);
                if (studyToRemove != null) {
                    em.remove(studyToRemove);
                } else {
                    throw new ApiException(404, "Study not found");
                }
                em.getTransaction().commit();
            }  catch (PersistenceException e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw new ApiException(500, "Delete study failed: " + e.getMessage());
            } catch (RuntimeException e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw e;
            }
        }
        return true;
    }
}
