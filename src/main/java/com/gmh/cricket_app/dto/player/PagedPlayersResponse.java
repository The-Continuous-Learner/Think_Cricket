package com.gmh.cricket_app.dto.player;

import java.util.List;

import com.gmh.cricket_app.models.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PagedPlayersResponse {
    private List<Player> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
