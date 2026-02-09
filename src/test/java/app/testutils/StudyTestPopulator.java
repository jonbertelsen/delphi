package app.testutils;

import app.entities.Study;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public final class StudyTestPopulator {

    private StudyTestPopulator() {}

    public static Map<String, Study> populate(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            em.createQuery("DELETE FROM Study").executeUpdate();

            Study study1 = new Study("Intro to JPA", 101, LocalDate.now().plusDays(1));
            Study study2 = new Study("Hibernate Basics", 102, LocalDate.now().plusDays(2));
            Study study3 = new Study("Entity Manager Deep Dive", 103, LocalDate.now().plusDays(3));

            em.persist(study1);
            em.persist(study2);
            em.persist(study3);
            em.flush();
            em.getTransaction().commit();

            Map<String, Study> seeded = new LinkedHashMap<>();
            seeded.put("study1", study1);
            seeded.put("study2", study2);
            seeded.put("study3", study3);
            return seeded;
        }
    }
}

