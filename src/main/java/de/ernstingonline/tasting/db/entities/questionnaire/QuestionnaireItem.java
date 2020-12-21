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
    private Long position;

    @ManyToOne()
    @JoinColumn(name = "questionnaire_id")
    private Questionnaire questionnaire;

    @OneToMany(mappedBy = "questionnaireItem")
    private Set<QuestionnaireAnswer> answers;

    @Enumerated(EnumType.ORDINAL)
    private QuestionType type;

    @Basic
    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public Long getId() {return id;}

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
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
