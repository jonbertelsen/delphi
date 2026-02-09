package app.entities;

import app.enums.StudyPhase;
import app.exceptions.ApiException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    private String title;
    private int teacherId;
    private LocalDate studyDate;

    @Enumerated(EnumType.ORDINAL)
    private StudyPhase phase;

    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate deletedAt;

    @PrePersist
    private void beforeCreate(){
        phase = StudyPhase.DIALOGUE;
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
        deletedAt = null;
    }

    @PreUpdate
    private void beforeUpdate(){
        updatedAt = LocalDate.now();
    }

    public Study(String title, int teacherId, LocalDate studyDate) {
        this.title = title;
        this.teacherId = teacherId;
        setStudyDate(studyDate);
    }

    private void setStudyDate(LocalDate studyDate){
        if (LocalDate.now().isAfter(studyDate)){
            throw new ApiException(422, "Study date should not be before " + LocalDate.now());
        }
        this.studyDate = studyDate;
    }
}
