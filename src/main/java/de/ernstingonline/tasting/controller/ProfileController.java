package de.ernstingonline.tasting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @RequestMapping("/")
    public String profile() {
        return "private/profile";
    }
}
