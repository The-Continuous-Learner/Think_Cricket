package com.gmh.cricket_app.models;

import com.gmh.cricket_app.enums.WicketType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "wicket")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Wicket {

    @Id
    private String id;

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "innings_id")
    private String inningsId;

    @Column(name = "over_id")
    private String overId;

    @Column(name = "ball_id")
    private String ballId;

    @Column(name = "player_out_id")
    private String playerOutId;

    @Column(name = "bowler_id")
    private String bowlerId;

    @Column(name = "fielder_id")
    private String fielderId;

    @Enumerated(EnumType.STRING)
    private WicketType type;
}
