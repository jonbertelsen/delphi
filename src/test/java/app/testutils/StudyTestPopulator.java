package app.testutils;

import app.entities.Study;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public final class StudyTestPopulator {

    private StudyTestPopulator() {}

    public static Map<String, Study> populate(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();
            LocalDate baseDate = LocalDate.of(2028,2,1);
            Study study1 = new Study("Intro to JPA", 101, baseDate.plusDays(1));
            Study study2 = new Study("Hibernate Basics", 102, baseDate.plusDays(2));
            Study study3 = new Study("Entity Manager Deep Dive", 103, baseDate.plusDays(3));

                try {
                    em.createNativeQuery("TRUNCATE TABLE study RESTART IDENTITY CASCADE").executeUpdate();
                    em.persist(study1);
                    em.persist(study2);
                    em.persist(study3);
                    em.flush();
                } catch (PersistenceException e) {
                    if (em.getTransaction().isActive()) em.getTransaction().rollback();
                    throw e;
                }
            em.getTransaction().commit();

            Map<String, Study> seeded = new LinkedHashMap<>();
            seeded.put("study1", study1);
            seeded.put("study2", study2);
            seeded.put("study3", study3);
            return seeded;
        }
    }
}

