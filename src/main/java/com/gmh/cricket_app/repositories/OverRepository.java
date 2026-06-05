package com.gmh.cricket_app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.enums.OverStatus;
import com.gmh.cricket_app.models.Over;

public interface OverRepository extends JpaRepository<Over, String> {

    List<Over> findByInningsIdOrderByOverNumberAsc(String inningsId);

    Optional<Over> findByInningsIdAndStatus(String inningsId, OverStatus status);
}
