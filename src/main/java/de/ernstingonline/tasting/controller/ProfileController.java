package de.ernstingonline.tasting.controller;

import de.ernstingonline.tasting.db.dao.game.PlayerDao;
import de.ernstingonline.tasting.db.entities.game.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private PlayerDao playerDao;

    @RequestMapping("/")
    public String profile(Principal principal,
                          Model model) {
        Player player = playerDao.findByUsername(principal.getName()).get(0);
        model.addAttribute("player", player);

        return "private/profile";
    }
}
