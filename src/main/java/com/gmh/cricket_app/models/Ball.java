package com.gmh.cricket_app.models;

import java.time.LocalDateTime;

import com.gmh.cricket_app.models.enums.DismissalType;
import com.gmh.cricket_app.models.enums.ExtraType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Ball {
    @Id
    private String id;

    private int batsmanRun;

    private int extraRun;

    @Enumerated(EnumType.STRING)
    private ExtraType extraType;

    private String batsmanId; 

    private String nonStrikerId;

    private String bowlerId;
    
    private boolean legalDelivery; 

    private String overId; 

    private int ballNumber;

    private boolean wicket;

    @Enumerated(EnumType.STRING)
    private DismissalType dismissalType; 
    private String dismissedBatsmanId;
    private String fielderId; 

    private boolean freeHit;

    private LocalDateTime timestamp;


    @PrePersist
    public void generateId() {
        this.id = overId + "-" + ballNumber;
        this.timestamp = LocalDateTime.now();
    }
}
