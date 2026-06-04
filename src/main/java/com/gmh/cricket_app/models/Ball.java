package com.gmh.cricket_app.models;

import com.gmh.cricket_app.enums.ExtraType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ball")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Ball {

    @Id
    private String id;

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "innings_id")
    private String inningsId;

    @Column(name = "over_id", insertable = false, updatable = false)
    private String overId;

    @Column(name = "innings_number")
    private int inningsNumber;

    @Column(name = "over_number")
    private int overNumber;

    @Column(name = "ball_number")
    private int ballNumber;

    @Column(name = "legal_delivery")
    private boolean legalDelivery;

    private int runs;

    @Enumerated(EnumType.STRING)
    @Column(name = "extra_type")
    private ExtraType extraType;

    @Column(name = "batsman_id")
    private String batsmanId;

    @Column(name = "non_striker_id")
    private String nonStrikerId;

    private boolean wicket;

    @OneToOne
    @JoinColumn(name = "wicket_id")
    private Wicket wicketInfo;

    @ManyToOne
    @JoinColumn(name = "over_id")
    private Over over;
}
