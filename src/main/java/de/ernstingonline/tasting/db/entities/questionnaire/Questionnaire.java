package de.ernstingonline.tasting.db.entities.questionnaire;

import javax.persistence.*;
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

}
