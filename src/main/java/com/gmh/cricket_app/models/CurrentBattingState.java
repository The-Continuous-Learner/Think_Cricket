package com.gmh.cricket_app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "current_batting_state")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentBattingState {

    @Id
    @Column(name = "innings_id")
    private String inningsId;

    @Column(name = "striker_id")
    private String strikerId;

    @Column(name = "non_striker_id")
    private String nonStrikerId;
}
