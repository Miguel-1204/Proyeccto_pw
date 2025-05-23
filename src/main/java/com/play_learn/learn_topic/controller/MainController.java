package com.play_learn.learn_topic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String root() {
        return "auth/login"; // Retorna directamente la plantilla de login
    }
    @GetMapping("/home")
    public String home(Model model) {
        return "home/home";
    }

	@GetMapping("/info")
	public String info(Model model) {
	    return "info/info";
	}
}
