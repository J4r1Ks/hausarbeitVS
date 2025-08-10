package de.hsos.vs.pong.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/error")
    public String error() {
        return "redirect:/login?error";
    }

}