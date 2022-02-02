package de.ernstingonline.tasting.controller;

import de.ernstingonline.tasting.db.dao.game.PlayerDao;
import de.ernstingonline.tasting.db.dao.game.TastingDao;
import de.ernstingonline.tasting.db.dao.questionnaire.QuestionnaireDao;
import de.ernstingonline.tasting.db.dao.samples.ProductDao;
import de.ernstingonline.tasting.db.entities.game.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private TastingDao tastingDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private QuestionnaireDao questionnaireDao;

    private static final String USERNOTFOUND = "User nicht gefunden";
    private static final String MSG = "messages";
    private static final String TOADMIN = "redirect:/admin/index";

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("playerCount", playerDao.count());
        model.addAttribute("tastingCount", tastingDao.count());
        model.addAttribute("productCount", productDao.count());
        model.addAttribute("questionnaireCount", questionnaireDao.count());
        model.addAttribute("newestUsers", playerDao.findTop5ByOrderByCreationDateDesc());
        return "admin/index";
    }

    @GetMapping("/users/{id}/edit")
    public String editUsers(@PathVariable("id") String id,
                            RedirectAttributes attrs,
                            Model model) {
        Optional<Player> optionalPlayer = playerDao.findById(Long.parseLong(id));
        if (!optionalPlayer.isPresent()) {
            attrs.addFlashAttribute(MSG, USERNOTFOUND);
            return TOADMIN;
        }

        Player player = optionalPlayer.get();
        model.addAttribute("player", player);
        return "admin/editUser";
    }

    @GetMapping("/users/{id}/edit/promote")
    public String promoteUser(@PathVariable("id") String id,
                              RedirectAttributes atts) {
        Optional<Player> optionalPlayer = playerDao.findById(Long.parseLong(id));

        if (!optionalPlayer.isPresent()) {
            atts.addFlashAttribute(MSG, USERNOTFOUND);
            return TOADMIN;
        }

        Player player = optionalPlayer.get();
        player.setAdmin(!player.isAdmin());
        playerDao.save(player);

        return "redirect:/admin/users/"+id+"/edit";
    }

    @GetMapping("/users/{id}/edit/grant")
    public String grantUser(@PathVariable("id") String id,
                            RedirectAttributes atts) {
        Optional<Player> optionalPlayer = playerDao.findById(Long.parseLong(id));

        if (!optionalPlayer.isPresent()) {
            atts.addFlashAttribute(MSG, USERNOTFOUND);
            return TOADMIN;
        }

        Player player = optionalPlayer.get();
        player.setCredit(player.getCredit() + 1);
        playerDao.save(player);

        return "redirect:/admin/users/"+id+"/edit";
    }
}
