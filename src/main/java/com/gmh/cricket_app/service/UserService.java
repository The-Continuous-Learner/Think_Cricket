package com.gmh.cricket_app.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.gmh.cricket_app.dto.LoginRequest;
import com.gmh.cricket_app.dto.LoginResponse;
import com.gmh.cricket_app.dto.RegistrationRequest;
import com.gmh.cricket_app.dto.RegistrationResponse;
import com.gmh.cricket_app.exceptions.BadRequestException;
import com.gmh.cricket_app.models.User.Session;
import com.gmh.cricket_app.models.User.User;
import com.gmh.cricket_app.repositories.UserRepository;
import com.gmh.cricket_app.util.PasswordUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final SessionService sessionService;

    public RegistrationResponse register(RegistrationRequest req) {

        // 1. Check if email/username already exists
        List<User> existing = userRepo.findByEmailOrUsername(req.getEmail(), req.getUsername());

        boolean emailExists = existing.stream().anyMatch(u -> u.getEmail().equals(req.getEmail()));
        boolean usernameExists = existing.stream().anyMatch(u -> u.getUsername().equals(req.getUsername()));

        if (emailExists) throw new BadRequestException("Email already registered");
        if (usernameExists) throw new BadRequestException("Username already taken");


        // 2. Create user
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPasswordHash(PasswordUtil.hashPassword(req.getPassword()));
        user.setVerified(true); // for now, auto-verify

        userRepo.save(user);

        return new RegistrationResponse(user.getId(), user.getUsername(), user.getEmail());
    }

    public LoginResponse login(LoginRequest req) {
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!PasswordUtil.verify(req.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        Session session = sessionService.createSession(user.getId());

        return new LoginResponse(
                session.getToken(),
                user.getId(),
                user.getUsername()
        );
    }

}

