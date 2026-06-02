package com.gmh.cricket_app.models.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class User {

    @Id
    private String id; // UUID

    private String username;
    private String email;

    private String passwordHash; // store only hashed password

    private boolean verified; // for future email verification
}
