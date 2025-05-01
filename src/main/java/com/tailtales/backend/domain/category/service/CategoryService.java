package com.tailtales.backend.domain.category.service;

import com.tailtales.backend.domain.category.dto.CategoriesResponseDto;
import com.tailtales.backend.domain.category.dto.CategoryRequestDto;
import com.tailtales.backend.domain.category.dto.CategoryUpdateRequestDto;

import java.util.List;

public interface CategoryService {

    Integer insertCategory(CategoryRequestDto categoryRequestDto);

    List<CategoriesResponseDto> getCategoryList();

    Integer updateCategory(Integer bcno, CategoryUpdateRequestDto categoryUpdateRequestDto);

    Integer deleteCategory(Integer bcno);

}
