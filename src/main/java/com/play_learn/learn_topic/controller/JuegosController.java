package com.play_learn.learn_topic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.play_learn.learn_topic.entity.Dificultad;
import com.play_learn.learn_topic.entity.PreguntaIngles;
import com.play_learn.learn_topic.service.JuegoInglesService;

@Controller
@RequestMapping("/juegos")
public class JuegosController {

    @Autowired
    private JuegoInglesService juegoInglesService;

    // Juego de inglés CON NIVELES
    @GetMapping("/ingles")
    public String mostrarJuegoIngles(
        @RequestParam(required = false, defaultValue = "PRINCIPIANTE") Dificultad nivel,
        Model model) {
        
        PreguntaIngles pregunta = juegoInglesService.getPreguntaPorNivel(nivel);
        
        if (pregunta == null) {
            model.addAttribute("puntuacion", juegoInglesService.getPuntuacion());
            model.addAttribute("nivel", nivel);
            juegoInglesService.reiniciarNivel(nivel);
            return "juegos/fin-nivel-ingles";
        }
        
        // Añade estas líneas aquí ▼
        model.addAttribute("preguntasMostradas", juegoInglesService.getPreguntasMostradasCount(nivel));
        model.addAttribute("totalPreguntasNivel", juegoInglesService.getTotalPreguntasNivel(nivel));
        // ▲
        
        model.addAttribute("pregunta", pregunta);
        model.addAttribute("niveles", Dificultad.values());
        return "juegos/ingles";
    }

    @PostMapping("/ingles/verificar")
    public String verificarRespuestaIngles(
        @RequestParam String oracion,
        @RequestParam String respuesta,
        @RequestParam Dificultad nivel,
        RedirectAttributes redirectAttrs) {
        
        boolean esCorrecta = juegoInglesService.verificarRespuesta(oracion, respuesta);
        juegoInglesService.registrarRespuesta(esCorrecta);
        
        redirectAttrs.addFlashAttribute("resultado", esCorrecta ? "¡Correcto!" : "Incorrecto");
        redirectAttrs.addAttribute("nivel", nivel);
        return "redirect:/juegos/ingles";
    }

    // Juego de matemáticas (sin cambios)
    @GetMapping("/matematicas")
    public String matematicas() {
        return "juegos/matematicas";
    }
}