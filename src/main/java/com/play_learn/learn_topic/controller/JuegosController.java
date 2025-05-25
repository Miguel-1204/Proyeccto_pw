package com.play_learn.learn_topic.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.play_learn.learn_topic.entity.Dificultad;
import com.play_learn.learn_topic.entity.PreguntaIngles;
import com.play_learn.learn_topic.entity.PuntuacionClasificacion;
import com.play_learn.learn_topic.entity.PuntuacionGeometria;
import com.play_learn.learn_topic.repository.PuntuacionClasificacionRepository;
import com.play_learn.learn_topic.repository.PuntuacionGeometriaRepository;
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

        // Obtener el usuario autenticado
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username); // Agregar el username al modelo

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
        model.addAttribute("pregunta", pregunta);
        model.addAttribute("niveles", Dificultad.values());

        System.out.println("Usuario autenticado: " + username);
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
        
        // Guarda la puntuación en la BD (¡asegúrate de estar autenticado!)
        String username = "admin"; // Aquí deberías obtener el username desde Spring Security
        juegoInglesService.guardarPuntuacion(username, nivel, esCorrecta ? 10 : 0);
        
        redirectAttrs.addFlashAttribute("resultado", esCorrecta ? "¡Correcto!" : "Incorrecto");
        redirectAttrs.addAttribute("nivel", nivel);
        return "redirect:/juegos/ingles";
    }
    

 // 🔹 Vista principal de Juegos de Matemáticas
    @GetMapping("/home-matematicas")
    public String homeMatematicas() {
        return "juegos/home-matematicas";
    }

    // 🔹 Juegos específicos de Matemáticas
    @GetMapping("/matematicas/operaciones")
    public String juegoOperaciones() {
        return "juegos/matematicas/operaciones";
    }

//    @GetMapping("/matematicas/geometria")
//    public String juegoGeometria() {
//        return "juegos/matematicas/geometria";
//    }
    @GetMapping("/matematicas/clasificacion")
    public String juegoClasificacion(Model model) {
        List<Integer> numeros = List.of(27, 98, 1023);
        List<String> imagenes = List.of("/img/numeros/27.jpg", "/img/numeros/98.jpg", "/img/numeros/1023.jpg");

        model.addAttribute("numeros", numeros);
        model.addAttribute("imagenes", imagenes);

        return "juegos/matematicas/clasificacion"; // Enlace correcto a la vista
    }

    @GetMapping("/matematicas/nueva-ronda")
    public String nuevaRonda(@RequestParam(defaultValue = "1") int ronda, 
                             @ModelAttribute("resultados") List<Integer> resultados, 
                             Model model) {
        List<List<Integer>> numerosPorRonda = List.of(
            List.of(27, 98, 1023),
            List.of(311, 9755, 532),
            List.of(907, 115, 7821),
            List.of(1024, 8080, 2025),
            List.of(3306, 1098, 241)
        );

        List<List<String>> imagenesPorRonda = List.of(
            List.of("/img/numeros/27.jpg", "/img/numeros/98.jpg", "/img/numeros/1023.jpg"),
            List.of("/img/numeros/311.png", "/img/numeros/9755.jpg", "/img/numeros/532.jpg"),
            List.of("/img/numeros/907.jpg", "/img/numeros/115.jpg", "/img/numeros/7821.jpg"),
            List.of("/img/numeros/1024.jpg", "/img/numeros/8080.jpg", "/img/numeros/2025.jpg"),
            List.of("/img/numeros/3306.jpg", "/img/numeros/1098.jpg", "/img/numeros/241.jpg")
        );

        if (ronda > 5) {
            return "redirect:/puntuaciones/clasificacion"; // Redirige a la pantalla final
        }

        // ✅ Agrega la ronda actual al modelo
        model.addAttribute("numeros", numerosPorRonda.get(ronda - 1));
        model.addAttribute("imagenes", imagenesPorRonda.get(ronda - 1));
        model.addAttribute("rondaActual", ronda); // ✅ Ahora disponible en la vista

        return "juegos/matematicas/clasificacion";
    }
    
    @Autowired
    private PuntuacionClasificacionRepository puntuacionRepository;

    	
    @PostMapping("/matematicas/guardar-puntuacion")
    public ResponseEntity<?> guardarPuntuacion(@RequestBody Map<String, Integer> datos) {
        int ronda = datos.get("ronda");
        int puntuacion = datos.get("puntuacion");

        // Obtén el username del usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        PuntuacionClasificacion puntuacionNueva = new PuntuacionClasificacion();
        puntuacionNueva.setUsuario(username); // Ahora se asigna el username real
        puntuacionNueva.setRonda(ronda);
        puntuacionNueva.setPuntuacion(puntuacion);

        puntuacionRepository.save(puntuacionNueva);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/puntuaciones/clasificacion")
    public String mostrarResultados(Model model) {
        List<PuntuacionClasificacion> resultados = puntuacionRepository.findAll(); // 📌 Recupera todas las puntuaciones de la BD
        model.addAttribute("resultados", resultados);

        return "juegos/puntuaciones/lista-clasificacion";
    }

    @PostMapping("/puntuaciones/eliminar")
    public String borrarResultados() {
        puntuacionRepository.deleteAll();
        return "redirect:/juegos/puntuaciones/clasificacion";
    }
    
    //Juego de geometria
    
    @Autowired
    private PuntuacionGeometriaRepository puntuacionGeometriaRepository;

    // Endpoint para mostrar el juego de memoria
    @GetMapping("/matematicas/geometria")
    public String juegoMemoria(Model model) {
        // Definimos 3 imágenes que aparecerán cada una dos veces
        List<String> imagenes = new ArrayList<>();
        imagenes.add("/img/figuras/Triangulo-equilatero.jpg");
        imagenes.add("/img/figuras/Triangulo-equilatero.jpg");
        imagenes.add("/img/figuras/pentagono.jpg");
        imagenes.add("/img/figuras/pentagono.jpg");
        imagenes.add("/img/figuras/rectangulo.jpg");
        imagenes.add("/img/figuras/rectangulo.jpg");
        
        // Barajar la lista para que la distribución sea aleatoria
        Collections.shuffle(imagenes);
        model.addAttribute("imagenes", imagenes);
        
        // Devuelve la vista, por ejemplo "juegos/matematicas/memoria"
        return "juegos/matematicas/geometria";
    }

    // Endpoint para guardar la puntuación del juego de memoria
    @PostMapping("/matematicas/guardar-puntuacion-memoria")
    public ResponseEntity<?> guardarPuntuacionMemoria(@org.springframework.web.bind.annotation.RequestBody Map<String, Integer> datos) {
        int tiempo = datos.get("tiempo");
        // Obtener el usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
    
        PuntuacionGeometria puntuacion = new PuntuacionGeometria();
        puntuacion.setUsername(username);
        puntuacion.setTiempo(tiempo);
    
        puntuacionGeometriaRepository.save(puntuacion);
    
        return ResponseEntity.ok().build();
    }


}