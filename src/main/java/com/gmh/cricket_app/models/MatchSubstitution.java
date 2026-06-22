package com.gmh.cricket_app.models;

import java.util.UUID;

import com.gmh.cricket_app.enums.SubstitutionType;

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

@Entity
@Table(name = "match_substitution")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchSubstitution {

    @Id
    private String id;

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "player_out_id")
    private String playerOutId;

    @Column(name = "player_in_id")
    private String playerInId;

    @Column(name = "innings_number")
    private int inningsNumber;

    @Column(name = "over_number")
    private int overNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "substitution_type")
    private SubstitutionType substitutionType;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        }
    }
}
