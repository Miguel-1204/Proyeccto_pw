package com.play_learn.learn_topic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/juegos")
public class JuegosController {
    @GetMapping("/ingles")
    public String ingles() {
        return "juegos/ingles"; // Plantilla vacía
    }

    @GetMapping("/matematicas")
    public String matematicas() {
        return "juegos/matematicas"; // Plantilla vacía
    }
}