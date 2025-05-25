package com.play_learn.learn_topic.service;

import com.play_learn.learn_topic.entity.PreguntaIngles;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class JuegoInglesService {
    
    private final List<PreguntaIngles> preguntas = Arrays.asList(
        new PreguntaIngles("I ___ to school every day", "go", Arrays.asList("go", "eat", "sleep")),
        new PreguntaIngles("She ___ a book right now", "is reading", Arrays.asList("reads", "is reading", "read"))
    );

    public PreguntaIngles getPreguntaAleatoria() {
        return preguntas.get(new Random().nextInt(preguntas.size()));
    }

    public boolean verificarRespuesta(String oracion, String respuesta) {
        return preguntas.stream()
            .anyMatch(p -> p.getOracion().equals(oracion) && 
                   p.getRespuestaCorrecta().equalsIgnoreCase(respuesta));
    }
}