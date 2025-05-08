package com.tailtales.backend.domain.category.service;

import com.tailtales.backend.domain.category.dto.CategoryChangeRequestDto;
import com.tailtales.backend.domain.category.dto.CategoriesResponseDto;
import com.tailtales.backend.domain.category.dto.CategoryRequestDto;
import com.tailtales.backend.domain.category.dto.CategoryUpdateRequestDto;

import java.util.List;

public interface CategoryService {

    void updateCategoryChanges(List<CategoryChangeRequestDto> categoryChangeRequestDto, String adminAccessToken);

    Integer insertCategory(CategoryRequestDto categoryRequestDto, String adminAccessToken);

    List<CategoriesResponseDto> getCategoryList(String adminAccessToken);

    Integer updateCategory(Integer bcno, CategoryUpdateRequestDto categoryUpdateRequestDto, String adminAccessToken);

    Integer deleteCategory(Integer bcno, String adminAccessToken);

}
