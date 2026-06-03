package com.gmh.cricket_app.models.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "User")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class User {

    @Id
    private String id;

    private String username;
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    private boolean verified;
}
