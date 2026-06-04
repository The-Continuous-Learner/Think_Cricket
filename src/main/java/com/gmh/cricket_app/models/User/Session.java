package com.gmh.cricket_app.models.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "sessions")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Session {

    @Id
    private String token;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "created_at", nullable = false)
    private long createdAt;

    @Column(name = "expires_at", nullable = false)
    private long expiresAt;

    public Session(String token, String userId, long expiresAt) {
        this.token = token;
        this.userId = userId;
        this.createdAt = System.currentTimeMillis();
        this.expiresAt = expiresAt;
    }
}


