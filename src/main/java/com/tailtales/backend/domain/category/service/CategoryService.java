package com.tailtales.backend.domain.category.service;

import com.tailtales.backend.domain.category.dto.CategoriesResponseDto;
import com.tailtales.backend.domain.category.dto.CategoryRequestDto;

import java.util.List;

public interface CategoryService {

    Integer insertCategory(CategoryRequestDto categoryRequestDto);

    List<CategoriesResponseDto> getCategoryList();

}
