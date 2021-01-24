package de.ernstingonline.tasting.controller;

import de.ernstingonline.tasting.db.entities.game.Player;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

@Controller
@RequestMapping("/intasting")
public class InTastingController {

    @RequestMapping("/{id}/index")
    public String tastingIndex(@PathVariable("id") String id) {
        return "intasting/index";
    }

    @MessageMapping("hello")
    @SendTo("topic/greetings")
    public Player greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        Player demo = new Player();
        demo.setName("demo");
        return demo;
    }

}

class HelloMessage {
    private String name;

    public HelloMessage() {
    }

    public HelloMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
