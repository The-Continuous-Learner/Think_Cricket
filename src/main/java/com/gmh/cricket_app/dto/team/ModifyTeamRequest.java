package com.gmh.cricket_app.dto.team;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class ModifyTeamRequest {
    private String sessionToken;
    private String teamId;
    private String name;          
    private String description;   
}

