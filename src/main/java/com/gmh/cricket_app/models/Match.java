package com.gmh.cricket_app.models;

import com.gmh.cricket_app.enums.MatchStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    private String id;

    @Column(name = "team_a_id", nullable = false)
    private String teamAId;

    @Column(name = "team_b_id", nullable = false)
    private String teamBId;

    @Column(nullable = false)
    private String format; // T20, ODI, TEST

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status;

    @Column(name = "planned_start_time", nullable = false)
    private long plannedStartTime;

    @Column(name = "actual_start_time")
    private Long actualStartTime;

    @Column(name = "actual_end_time")
    private Long actualEndTime;

    @Column(name = "hosted_by_user_id", nullable = false)
    private String hostedByUserId;

    @Column(name = "started_by_user_id")
    private String startedByUserId;

    @Column(name = "ended_by_user_id")
    private String endedByUserId;
}

