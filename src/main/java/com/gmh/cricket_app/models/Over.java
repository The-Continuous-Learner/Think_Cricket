package com.gmh.cricket_app.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Over {

    @Id
    private String id; // matchId-INN-1-OVER-5

    private String matchId;
    private String inningsId;

    private int inningsNumber;
    private int overNumber;

    @OneToMany
    private List<Ball> balls = new ArrayList<>();
}

