package com.gmh.cricket_app.models.team;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "team")
public class Team {
    @Id
    private String id;

    private String name;

    private String description;

    @Column(name = "created_by_user_id")
    private String createdByUserId;
}
