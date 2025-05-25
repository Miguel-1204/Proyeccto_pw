package com.play_learn.learn_topic.entity;

import java.util.List; // Este es el import esencial

public class PreguntaIngles {
    private String oracion;
    private String respuestaCorrecta;
    private List<String> opciones;

    // Constructor
    public PreguntaIngles(String oracion, String respuestaCorrecta, List<String> opciones) {
        this.oracion = oracion;
        this.respuestaCorrecta = respuestaCorrecta;
        this.opciones = opciones;
    }

    // Getters y Setters (usa @Data de Lombok si lo tienes)
    public String getOracion() { return oracion; }
    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public List<String> getOpciones() { return opciones; }
}