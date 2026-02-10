package app;

import app.config.HibernateConfig;
import app.daos.IDAO;
import app.daos.StudyDAO;
import app.entities.Study;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;

public class Main {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public static void main(String[] args) {

        IDAO<Study, Integer> studyDAO = new StudyDAO(emf);

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

                Study study1 = studyDAO.create(new Study("Intro to JPA", 101, LocalDate.now().plusDays(1)));
                Study study2 = studyDAO.create(new Study("Hibernate Basics", 102, LocalDate.now().plusDays(2)));
                Study study3 = studyDAO.create(new Study("Entity Manager Deep Dive", 103, LocalDate.now().plusDays(3)));

                System.out.println(study1);
                System.out.println(study2);
                System.out.println(study3);

            em.getTransaction().commit();
        }

        // Close the database connection:
        emf.close();
    }
}