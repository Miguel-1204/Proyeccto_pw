package com.play_learn.learn_topic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.play_learn.learn_topic.entity.PuntuacionGeometria;

@Repository
public interface PuntuacionGeometriaRepository extends JpaRepository<PuntuacionGeometria, Long> {
    List<PuntuacionGeometria> findByUsername(String username);
}
