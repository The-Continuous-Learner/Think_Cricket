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
public class Over {
    @Id
    private String id;

    private String matchId;

    private int overNumber;

    private int inningsNumber;

    private String bowlerId; 

    @PrePersist
    public void generateId() {
        this.id = matchId + "-" + inningsNumber + "-" + overNumber;
    }
}
