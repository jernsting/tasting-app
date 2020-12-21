package de.ernstingonline.tasting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/intasting/")
public class InTastingController {

    @RequestMapping("{id}/index")
    public String tastingIndex(@PathVariable("id") String id) {
        return "intasting/index";
    }

}
