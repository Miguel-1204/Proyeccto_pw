package com.play_learn.learn_topic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.play_learn.learn_topic.service.JuegoInglesService;

@Controller
@RequestMapping("/juegos")
public class JuegosController {

    @Autowired
    private JuegoInglesService juegoInglesService;

    // Juego de inglés
    @GetMapping("/ingles")
    public String mostrarJuegoIngles(Model model) {
        model.addAttribute("pregunta", juegoInglesService.getPreguntaAleatoria());
        return "juegos/ingles";
    }

    @PostMapping("/ingles/verificar")
    public String verificarRespuestaIngles(
        @RequestParam String oracion,
        @RequestParam String respuesta,
        RedirectAttributes redirectAttrs) {
        
        boolean esCorrecta = juegoInglesService.verificarRespuesta(oracion, respuesta);
        redirectAttrs.addFlashAttribute("resultado", esCorrecta ? "¡Correcto!" : "Incorrecto");
        return "redirect:/juegos/ingles";
    }

    // Juego de matemáticas (si lo necesitas)
    @GetMapping("/matematicas")
    public String matematicas() {
        return "juegos/matematicas";
    }
}