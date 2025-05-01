package com.tailtales.backend.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesResponseDto {

    private int bcno;

    private String name;

    private int parentBcno;

    private int depth;

}
