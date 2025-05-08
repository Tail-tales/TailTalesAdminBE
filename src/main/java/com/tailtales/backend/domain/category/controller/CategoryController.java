package com.tailtales.backend.domain.category.controller;

import com.tailtales.backend.domain.category.dto.CategoryChangeRequestDto;
import com.tailtales.backend.domain.category.dto.CategoriesResponseDto;
import com.tailtales.backend.domain.category.dto.CategoryRequestDto;
import com.tailtales.backend.domain.category.dto.CategoryUpdateRequestDto;
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

    @PostMapping("/changes")
    public ResponseEntity<Void> updateCategoryChanges(
            @RequestBody List<CategoryChangeRequestDto> categoryChangeRequestDto,
            @RequestHeader("Authorization") String adminAccessToken) {
        categoryService.updateCategoryChanges(categoryChangeRequestDto, adminAccessToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 카테고리 생성
    @PostMapping
    public ResponseEntity<Integer> insertCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto,
                                                  @RequestHeader("Authorization") String adminAccessToken) {

        Integer categoryId = categoryService.insertCategory(categoryRequestDto, adminAccessToken);
        return new ResponseEntity<>(categoryId, HttpStatus.CREATED);

    }

    // 카테고리 조회
    @GetMapping("/all")
    public ResponseEntity<List<CategoriesResponseDto>> getAllCategory(@RequestHeader("Authorization") String adminAccessToken) {

        List<CategoriesResponseDto> categoryList = categoryService.getCategoryList(adminAccessToken);
        return new ResponseEntity<>(categoryList, HttpStatus.OK);

    }

    // 카테고리 수정
    @PutMapping("/edit/{bcno}")
    public ResponseEntity<Integer> updateCategory(@PathVariable(name = "bcno") Integer bcno,
                                                  @Valid @RequestBody CategoryUpdateRequestDto categoryUpdateRequestDto,
                                                  @RequestHeader("Authorization") String adminAccessToken) {

        Integer categoryId = categoryService.updateCategory(bcno, categoryUpdateRequestDto, adminAccessToken);
        return new ResponseEntity<>(categoryId, HttpStatus.OK);

    }

    // 카테고리 삭제
    @DeleteMapping("/{bcno}")
    public ResponseEntity<Integer> deleteCategory(@PathVariable(name = "bcno") Integer bcno,
                                                  @RequestHeader("Authorization") String adminAccessToken) {

        Integer categoryId = categoryService.deleteCategory(bcno, adminAccessToken);
        return new ResponseEntity<>(categoryId, HttpStatus.OK);

    }

}
