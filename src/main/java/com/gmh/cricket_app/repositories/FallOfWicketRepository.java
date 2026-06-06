package com.gmh.cricket_app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.FallOfWicket;

public interface FallOfWicketRepository extends JpaRepository<FallOfWicket, String> {

    List<FallOfWicket> findByInningsIdOrderByWicketNumberAsc(String inningsId);

    long countByInningsId(String inningsId);
}
