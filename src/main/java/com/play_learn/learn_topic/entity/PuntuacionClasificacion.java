package com.play_learn.learn_topic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resultados_clasificacion")
@Getter @Setter
@NoArgsConstructor
public class PuntuacionClasificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario; // Puedes usar "anonimo" si no manejas usuarios registrados
    private int ronda;
    private int puntuacion;

 
}

