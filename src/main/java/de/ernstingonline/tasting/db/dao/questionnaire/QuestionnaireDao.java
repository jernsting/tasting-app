package de.ernstingonline.tasting.db.dao.questionnaire;

import de.ernstingonline.tasting.db.entities.questionnaire.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionnaireDao extends JpaRepository<Questionnaire, Long> {
}
