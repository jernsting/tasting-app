package de.ernstingonline.tasting.db.dao.questionnaire;

import de.ernstingonline.tasting.db.entities.questionnaire.QuestionnaireAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionaireAnswerDao extends JpaRepository<QuestionnaireAnswer, Long> {
}
