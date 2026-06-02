package com.gmh.cricket_app.models;

import com.gmh.cricket_app.enums.WicketType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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
public class Wicket {

    @Id
    private String id; // matchId-INN-1-OVER-5-BALL-3-WICKET

    private String matchId;
    private String inningsId;
    private String overId;
    private String ballId;

    private String playerOutId;
    private String bowlerId;
    private String fielderId;

    @Enumerated(EnumType.STRING)
    private WicketType type;
}

