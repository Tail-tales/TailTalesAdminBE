package com.tailtales.backend.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDto {

    private long bno;

    private String title;

    private String name;

    private String content;

    private int viewCnt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<String> categories;

}
