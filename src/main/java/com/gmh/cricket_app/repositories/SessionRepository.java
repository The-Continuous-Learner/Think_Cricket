package com.gmh.cricket_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gmh.cricket_app.models.User.Session;

public interface SessionRepository extends JpaRepository<Session, String> {
}

