package com.tailtales.backend.domain.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardListResponseDto {

    private String title;

    private String name;

    private int viewCnt;

    private LocalDateTime createdAt;

}
