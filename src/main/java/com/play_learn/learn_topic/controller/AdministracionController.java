package com.play_learn.learn_topic.controller;

import com.play_learn.learn_topic.entity.Puntuacion;
import com.play_learn.learn_topic.entity.PuntuacionClasificacion;
import com.play_learn.learn_topic.entity.PuntuacionGeografia;
import com.play_learn.learn_topic.entity.PuntuacionGeometria;
import com.play_learn.learn_topic.entity.Usuario;
import com.play_learn.learn_topic.repository.UsuarioRepository;

import com.play_learn.learn_topic.repository.PuntuacionClasificacionRepository;
import com.play_learn.learn_topic.repository.PuntuacionGeometriaRepository;
import com.play_learn.learn_topic.repository.PuntuacionRepository;
import com.play_learn.learn_topic.repository.PuntuacionGeografiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/administracion")
public class AdministracionController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PuntuacionRepository puntuacionRepository;
    
    @Autowired
    private PuntuacionClasificacionRepository puntuacionClasificacionRepository;
    
    @Autowired
    private PuntuacionGeometriaRepository puntuacionGeometriaRepository;
    
    @Autowired
    private PuntuacionGeografiaRepository puntuacionGeografiaRepository;
    
    // Método que muestra el panel de administración con la lista de usuarios
    @GetMapping()
    public String panelAdministracion(Authentication authentication, Model model) {
        if (authentication == null ||
            authentication.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR") ||
                                a.getAuthority().equals("ROLE_EDUCADOR"))) {
            throw new AccessDeniedException("Acceso denegado");
        }
        
        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        
        return "administracion";
    }

    
    // Método para buscar las puntuaciones por usuario (consulta a todos los repositorios de puntuaciones)
    @GetMapping("/buscar-puntuacion")
    public String buscarPuntuacion(@RequestParam("username") String username,
                                   Authentication authentication,
                                   Model model) {
        // Comprobar autorización
        if (authentication == null ||
            authentication.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR") ||
                                a.getAuthority().equals("ROLE_EDUCADOR"))) {
            throw new AccessDeniedException("Acceso denegado");
        }
        
        // Consultar puntuaciones por cada juego
        // Para la entidad Puntuacion (supongamos que es el juego de "Inglés")
        List<Puntuacion> puntuacionesIngles = puntuacionRepository.findByUsuarioUsername(username);
        
        // Para Clasificación
        List<PuntuacionClasificacion> puntuacionesClasificacion =
                puntuacionClasificacionRepository.findByUsuario(username);
        
        // Para Geometría (la entidad tiene: id, username, tiempo, victoria y fecha)
        List<PuntuacionGeometria> puntuacionesGeometria =
                puntuacionGeometriaRepository.findByUsername(username);
        
        // Para Geografía (la entidad tiene: id, username, victoria y fecha)
        List<PuntuacionGeografia> puntuacionesGeografia =
                puntuacionGeografiaRepository.findByUsername(username);
        
        // Agregar al modelo cada lista con un nombre representativo
        model.addAttribute("puntuacionesIngles", puntuacionesIngles);
        model.addAttribute("puntuacionesClasificacion", puntuacionesClasificacion);
        model.addAttribute("puntuacionesGeometria", puntuacionesGeometria);
        model.addAttribute("puntuacionesGeografia", puntuacionesGeografia);
        // Agregamos también el username para usarlo en la vista
        model.addAttribute("username", username);
        
        // Se retorna la misma vista de administración que contiene las dos secciones
        return "administracion";
    }

}
