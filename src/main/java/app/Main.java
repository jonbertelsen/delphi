package app;

import app.config.HibernateConfig;
import app.daos.IDAO;
import app.daos.StudyDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class Main {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public static void main(String[] args) {

        EntityManager em = emf.createEntityManager();
        IDAO studyDAO = new StudyDAO(emf);



        // Close the database connection:
        em.close();
        emf.close();
    }
}