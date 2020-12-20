package de.ernstingonline.tasting.db.entities.questionnaire;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "items")
public class QuestionnaireItem {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @ManyToOne()
    @JoinColumn(name = "questionnaire_id")
    private Questionnaire questionnaire;

    @OneToMany(mappedBy = "questionnaireItem")
    private Set<QuestionnaireAnswer> answers;

    public Long getId() {return id;}

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Basic
    public String getQuestion() {
        return question;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public Set<QuestionnaireAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<QuestionnaireAnswer> answers) {
        this.answers = answers;
    }
}
