package com.gmh.cricket_app.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "over")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Over {

    @Id
    private String id;

    @Column(name = "match_id")
    private String matchId;

    @Column(name = "innings_id")
    private String inningsId;

    @Column(name = "innings_number")
    private int inningsNumber;

    @Column(name = "over_number")
    private int overNumber;

    @OneToMany
    @JoinTable(
        name = "over_balls",
        joinColumns = @JoinColumn(name = "over_id"),
        inverseJoinColumns = @JoinColumn(name = "balls_id")
    )
    private List<Ball> balls = new ArrayList<>();
}
