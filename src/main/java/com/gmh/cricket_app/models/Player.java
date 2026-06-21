package com.gmh.cricket_app.models;

import java.util.UUID;

import com.gmh.cricket_app.enums.Gender;
import com.gmh.cricket_app.enums.PlayerTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
@Table(name = "player")
public class Player {

    @Id
    private String id;

    private String name;
    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private PlayerTypes type;

    @Column(name = "created_by_user_id")
    private String createdByUserId;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        }
    }
}
