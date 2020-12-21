package de.ernstingonline.tasting.controller;

import de.ernstingonline.tasting.db.dao.game.PlayerDao;
import de.ernstingonline.tasting.db.dao.game.TastingDao;
import de.ernstingonline.tasting.db.entities.game.Player;
import de.ernstingonline.tasting.db.entities.game.Tasting;
import de.ernstingonline.tasting.validators.InviteValidator;
import de.ernstingonline.tasting.validators.PlayerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class GreetingController {
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private TastingDao tastingDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Login controller
     * @return Login site
     */
    @RequestMapping("/login")
    public String login() {
        return "public/login";
    }

    @RequestMapping("/team")
    public String team() { return "public/team"; }

    @RequestMapping("/changelog")
    public String changelog() { return "public/changelog"; }

    @RequestMapping("/tutorial")
    public String tutorial() { return "public/tutorial"; }

    /**
     * Index controller
     * @return Index site
     */
    @RequestMapping("/")
    public String index(Model model,
                        Principal principal) {
        if (!isAuthenticated()) {
            return "public/index";
        }
        else {
            Player player = playerDao.findByUsername(principal.getName()).get(0);
            Set<Tasting> tastingSet = tastingDao.findByPlayersIsContaining(player);
            Set<Tasting> upcoming = new HashSet<>();
            tastingSet.forEach(tasting -> {
                if (tasting.getDate().after(new Date()))
                        upcoming.add(tasting);
            });
            model.addAttribute("upcoming", upcoming);
            model.addAttribute("invite", new InviteValidator());
            model.addAttribute("player", player);
            return "private/index";
        }
    }

    /**
     * register controller
     * @return Register site
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("registration", new PlayerValidator());
        return "public/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerSubmit(@Valid @ModelAttribute("registration") PlayerValidator registration,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "public/register";
        }

        Player player = new Player();
        player.setUsername(registration.getUsername());
        player.setName(registration.getDisplayname());
        player.setPassword(passwordEncoder.encode(registration.getPassword()));

        if (playerDao.count() == 0) {
            player.setAdmin(true);
        }

        playerDao.save(player);

        return "redirect:/login";
    }

    // HELPER FUNCTIONS

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
}
