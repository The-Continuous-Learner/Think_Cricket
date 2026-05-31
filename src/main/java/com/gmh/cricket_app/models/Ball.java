package com.gmh.cricket_app.models;

import java.util.UUID;

import com.gmh.cricket_app.models.enums.ExtraType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Ball {

    @Id
    private String id; // matchId-INN-1-OVER-5-BALL-3

    private String matchId;
    private String inningsId;
    private String overId;

    private int inningsNumber;
    private int overNumber;
    private int ballNumber;

    private boolean legalDelivery;
    private int runs;

    @Enumerated(EnumType.STRING)
    private ExtraType extraType;

    private String batsmanId;
    private String nonStrikerId;

    private boolean wicket;

    @OneToOne
    private Wicket wicketInfo;

    @ManyToOne
    private Over over;
}

