package de.ernstingonline.tasting.controller;

import de.ernstingonline.tasting.db.dao.game.PlayerDao;
import de.ernstingonline.tasting.db.entities.game.Player;
import de.ernstingonline.tasting.validators.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String profile(Principal principal,
                          Model model) {
        Player player = playerDao.findByUsername(principal.getName()).get(0);
        model.addAttribute("player", player);
        model.addAttribute("passwordObj", new PasswordValidator());

        return "private/profile";
    }

    @PostMapping(value = "/update/displayname")
    public String updateUsername(Principal principal,
                                 @ModelAttribute("name") String name,
                                 RedirectAttributes atts) {
        Player player = playerDao.findByUsername(principal.getName()).get(0);
        player.setName(name);
        playerDao.save(player);

        atts.addFlashAttribute("successes", "Anzeigename geändert");
        return "redirect:/profile/";
    }

    @PostMapping(value = "/update/password")
    public String updatePassword(Principal principal,
                                 RedirectAttributes atts,
                                 Model model,
                                 @Valid @ModelAttribute("passwordObj") PasswordValidator passwordValidator,
                                 BindingResult result){
        Player player = playerDao.findByUsername(principal.getName()).get(0);
        if (result.hasErrors()) {
            model.addAttribute("player", player);
            return "private/profile";
        }
        player.setPassword(passwordEncoder.encode(passwordValidator.getPassword()));
        playerDao.save(player);

        atts.addFlashAttribute("successes", "Passwort erfolgreich geändert");
        return "redirect:/profile/";
    }
}
