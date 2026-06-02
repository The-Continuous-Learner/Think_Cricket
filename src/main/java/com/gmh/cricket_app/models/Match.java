package com.gmh.cricket_app.models;

import java.util.Date;
import java.util.UUID;

import com.gmh.cricket_app.enums.MatchStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Match {

    @Id
    private String id;

    @ManyToOne
    private Team teamA;

    @ManyToOne
    private Team teamB;

    private MatchStatus status;

    @OneToOne
    private Innings firstInnings;

    @OneToOne
    private Innings secondInnings;

    private Date startTime;
    private Date endTime;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID()
                          .toString()
                          .replace("-", "")
                          .substring(0, 16)
                          .toUpperCase();
        }
    }
}

