package com.gmh.cricket_app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.Wicket;

public interface WicketRepository extends JpaRepository<Wicket, String> {

    List<Wicket> findByInningsId(String inningsId);

    Optional<Wicket> findByBallId(String ballId);
}
