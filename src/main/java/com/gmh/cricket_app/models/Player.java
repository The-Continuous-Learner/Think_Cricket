package com.gmh.cricket_app.models;

import java.util.UUID;

import com.gmh.cricket_app.models.enums.Gender;
import com.gmh.cricket_app.models.enums.PlayerTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class Player {

    @Id
    private String id;

    private String name;
    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private PlayerTypes type;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }
}
