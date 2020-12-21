package de.ernstingonline.tasting.controller;

import de.ernstingonline.tasting.db.dao.questionnaire.QuestionnaireDao;
import de.ernstingonline.tasting.db.entities.questionnaire.Questionnaire;
import de.ernstingonline.tasting.exceptions.QuestionnaireNotFoundException;
import de.ernstingonline.tasting.validators.QuestionnaireValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/question")
public class QuestionnaireController {

    @Autowired
    private QuestionnaireDao questionnaireDao;

    @RequestMapping("/")
    public String listQuestionnaire(Model model) {
        model.addAttribute("questionnairs", questionnaireDao.findAll());
        return "questionnaire/listQuestionnaire";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newQuestionnaire(Model model) {
        model.addAttribute("questionnaire", new QuestionnaireValidator());
        return "questionnaire/newQuestionnaire";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String createQuestionnaire(@Valid @ModelAttribute("questionnaire") QuestionnaireValidator questionnaireValidator,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "questionnaire/newQuestionnaire";

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setTitle(questionnaireValidator.getTitle());
        questionnaireDao.save(questionnaire);
        return "redirect:/question/";
    }

    @RequestMapping("/{id}/view")
    public String viewQuestionnaire(@PathVariable("id") String id,
                                    Model model) {
        Optional<Questionnaire> optionalQuestionnaire = questionnaireDao.findById(Long.parseLong(id));
        if (!optionalQuestionnaire.isPresent())
            throw new QuestionnaireNotFoundException();

        model.addAttribute("questionnaire", optionalQuestionnaire.get());

        return "questionnaire/viewQuestionnaire";
    }

}
