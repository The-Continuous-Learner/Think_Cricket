package com.gmh.cricket_app.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmh.cricket_app.dto.LoginRequest;
import com.gmh.cricket_app.dto.LoginResponse;
import com.gmh.cricket_app.dto.RegistrationRequest;
import com.gmh.cricket_app.service.SessionService;
import com.gmh.cricket_app.service.UserService;
import com.gmh.cricket_app.dto.RegistrationResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SessionService sessionService;

    @PostMapping("/register")
    public RegistrationResponse register(@RequestBody RegistrationRequest req) {
        return userService.register(req);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        return userService.login(req);
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Session-Token") String token) {
        sessionService.deleteSession(token);
    }

    @PostMapping("/validateSession")
    public void validateSession(@RequestHeader("Session-Token") String token) {
        sessionService.validateSession(token);
    }

}

