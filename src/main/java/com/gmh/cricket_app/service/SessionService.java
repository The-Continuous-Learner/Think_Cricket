package com.gmh.cricket_app.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.exceptions.SessionExpiredException;
import com.gmh.cricket_app.models.User.Session;
import com.gmh.cricket_app.models.User.User;
import com.gmh.cricket_app.repositories.SessionRepository;
import com.gmh.cricket_app.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepo;
    private final UserRepository userRepo;

    @Value("${session.duration.ms}")
    private long sessionDurationMs;

    public Session createSession(String userId) {

        long now = System.currentTimeMillis();

        Session session = new Session();
        session.setToken(UUID.randomUUID().toString());
        session.setUserId(userId);
        session.setCreatedAt(now);
        session.setExpiresAt(now + sessionDurationMs);

        sessionRepo.save(session);
        return session;
    }

    public User validateSession(String token) {
        Session session = sessionRepo.findById(token)
                .orElseThrow(() -> new BadRequestException("Invalid session token"));

        if (session.getExpiresAt() < System.currentTimeMillis()) {
            sessionRepo.delete(session);
            throw new SessionExpiredException("Session expired");
        }

        return userRepo.findById(session.getUserId())
                .orElseThrow(() -> new BadRequestException("User not found"));
    }

    public void deleteSession(String token) {
        if (sessionRepo.existsById(token)) {
            sessionRepo.deleteById(token);
        }
    }

}