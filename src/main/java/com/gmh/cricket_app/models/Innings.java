package com.gmh.cricket_app.models;

import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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

public class Innings {

    @Id
    private String id;

    private String matchId;      
    private String battingTeamId; 
    private String bowlingTeamId;

    private int inningsNumber;   

    private int totalRuns;
    private int totalWickets;
    private int totalOvers;      

    @PrePersist
    public void generateId() {
        this.id = matchId + "-" + inningsNumber;
    }
}
