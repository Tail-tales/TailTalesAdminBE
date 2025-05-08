package com.tailtales.backend.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryChangeRequestDto {

    private String operationType;
    private Integer bcno; // UPDATE, DELETE 시 필요
    private String name;   // CREATE, UPDATE 시 필요
    private Integer parentBcno; // CREATE, UPDATE 시 필요

}
