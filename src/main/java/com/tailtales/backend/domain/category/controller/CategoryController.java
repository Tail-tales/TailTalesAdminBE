package com.tailtales.backend.domain.category.controller;

import com.tailtales.backend.domain.category.dto.CategoriesResponseDto;
import com.tailtales.backend.domain.category.dto.CategoryRequestDto;
import com.tailtales.backend.domain.category.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/categories")
@Tag(name = "Category", description = "Category API")
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 생성
    @PostMapping
    public ResponseEntity<Integer> insertCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {

        Integer categoryId = categoryService.insertCategory(categoryRequestDto);
        return new ResponseEntity<>(categoryId, HttpStatus.CREATED);

    }

    // 카테고리 조회
    @GetMapping("/all")
    public ResponseEntity<List<CategoriesResponseDto>> getAllCategory() {

        List<CategoriesResponseDto> categoryList = categoryService.getCategoryList();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);

    }

}
