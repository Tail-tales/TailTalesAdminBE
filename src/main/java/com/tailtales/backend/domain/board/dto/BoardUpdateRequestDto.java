package com.tailtales.backend.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardUpdateRequestDto {

    private long bno;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    // 유효성 검사 추가 할지 말지
    private List<Integer> categories;

}
