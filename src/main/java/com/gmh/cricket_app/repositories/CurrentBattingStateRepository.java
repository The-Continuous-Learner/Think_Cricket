package com.gmh.cricket_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gmh.cricket_app.models.CurrentBattingState;

public interface CurrentBattingStateRepository extends JpaRepository<CurrentBattingState, String> {
}
