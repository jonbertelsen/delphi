package app.daos;

import app.config.HibernateTestConfig;
import app.entities.Study;
import app.exceptions.ApiException;
import app.testutils.StudyTestPopulator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudyDAOTest {

    private final EntityManagerFactory emf = HibernateTestConfig.getEntityManagerFactory();

    private StudyDAO studyDAO;
    private Map<String, Study> seeded;

    @BeforeEach
    void beforeEach(){
        seeded = StudyTestPopulator.populate(emf);
        studyDAO = new StudyDAO(emf);
    }

    @AfterAll
    void shutdown() {
        emf.close();
    }

    @Test
    void create() {
        Study study = new Study("JPA Testing", 200, LocalDate.now().plusDays(5));

        Study created = studyDAO.create(study);

        assertThat(created.getId(), notNullValue());
        Study fetched = studyDAO.getById(created.getId());
        assertThat(fetched.getTitle(), is("JPA Testing"));
        assertThat(fetched.getTeacherId(), is(200));
    }

    @Test
    void getById() {
        Study seed = seeded.get("study1");
        Study fetched = studyDAO.getById(seed.getId());
        assertThat(fetched.getId(), is(seed.getId()));
        assertThat(fetched.getTitle(), is(seed.getTitle()));
    }

    @Test
    void getAll() {
        List<Study> all = studyDAO.getAll();
        assertThat(all, hasSize(3));
        assertThat(all, containsInAnyOrder(seeded.get("study1"), seeded.get("study2"), seeded.get("study3")));
    }

    @Test
    void update() {
        Study seed = seeded.get("study2");
        LocalDate priorUpdatedAt = LocalDate.now().minusDays(2);
        Study updated = Study.builder()
                .id(seed.getId())
                .title("Updated Title")
                .teacherId(seed.getTeacherId())
                .studyDate(seed.getStudyDate())
                .phase(seed.getPhase())
                .createdAt(seed.getCreatedAt())
                .updatedAt(priorUpdatedAt)
                .deletedAt(seed.getDeletedAt())
                .build();

        Study result = studyDAO.update(updated);

        assertThat(result.getId(), is(seed.getId()));
        assertThat(result.getTitle(), is("Updated Title"));
        assertThat(result.getUpdatedAt(), notNullValue());
        assertThat(result.getUpdatedAt(), is(LocalDate.now()));
        assertThat(result.getUpdatedAt(), not(priorUpdatedAt));
    }

    @Test
    void delete() {
        Study seed = seeded.get("study3");

        boolean deleted = studyDAO.delete(seed.getId());

        assertThat(deleted, is(true));
        assertThrows(ApiException.class, () -> studyDAO.getById(seed.getId()));
    }

    @Test
    void create_withNullStudy_throwsApiException() {
        ApiException ex = assertThrows(ApiException.class, () -> studyDAO.create(null));
        assertThat(ex.getCode(), is(400));
    }

    @Test
    void getById_withNullId_throwsApiException() {
        ApiException ex = assertThrows(ApiException.class, () -> studyDAO.getById(null));
        assertThat(ex.getCode(), is(400));
    }

    @Test
    void getById_withMissingId_throwsApiException() {
        ApiException ex = assertThrows(ApiException.class, () -> studyDAO.getById(999_999));
        assertThat(ex.getCode(), is(404));
    }

    @Test
    void update_withNullStudy_throwsApiException() {
        ApiException ex = assertThrows(ApiException.class, () -> studyDAO.update(null));
        assertThat(ex.getCode(), is(400));
    }

    @Test
    void update_withMissingId_throwsApiException() {
        Study missing = Study.builder()
                .id(999_999)
                .title("Missing")
                .teacherId(1)
                .studyDate(LocalDate.now().plusDays(1))
                .build();

        ApiException ex = assertThrows(ApiException.class, () -> studyDAO.update(missing));
        assertThat(ex.getCode(), is(404));
    }

    @Test
    void delete_withNullId_throwsApiException() {
        ApiException ex = assertThrows(ApiException.class, () -> studyDAO.delete(null));
        assertThat(ex.getCode(), is(400));
    }

    @Test
    void delete_withMissingId_throwsApiException() {
        ApiException ex = assertThrows(ApiException.class, () -> studyDAO.delete(999_999));
        assertThat(ex.getCode(), is(404));
    }
}