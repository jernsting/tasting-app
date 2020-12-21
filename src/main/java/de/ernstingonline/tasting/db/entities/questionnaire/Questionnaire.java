package de.ernstingonline.tasting.db.entities.questionnaire;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "questionnaire")
public class Questionnaire {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(mappedBy = "questionnaire")
    private Set<QuestionnaireItem> questions = new HashSet<>();

    private String title;
    private Date creationDate = new Date();

    @Basic
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Set<QuestionnaireItem> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<QuestionnaireItem> questions) {
        this.questions = questions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
