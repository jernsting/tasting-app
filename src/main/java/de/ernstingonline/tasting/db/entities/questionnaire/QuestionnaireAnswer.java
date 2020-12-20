package de.ernstingonline.tasting.db.entities.questionnaire;

import javax.persistence.*;

@Entity
public class QuestionnaireAnswer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;

    @ManyToOne()
    @JoinColumn(name = "item")
    private QuestionnaireItem questionnaireItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
