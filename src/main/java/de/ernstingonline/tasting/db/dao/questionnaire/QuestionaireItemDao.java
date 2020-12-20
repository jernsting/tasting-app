package de.ernstingonline.tasting.db.dao.questionnaire;

import de.ernstingonline.tasting.db.entities.questionnaire.QuestionnaireItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionaireItemDao extends JpaRepository<QuestionnaireItem, Long> {
}
