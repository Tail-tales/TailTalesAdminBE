package com.tailtales.backend.domain.category.service.impl;

import com.tailtales.backend.domain.admin.service.AdminService;
import com.tailtales.backend.domain.category.dto.CategoriesResponseDto;
import com.tailtales.backend.domain.category.dto.CategoryRequestDto;
import com.tailtales.backend.domain.category.dto.CategoryUpdateRequestDto;
import com.tailtales.backend.domain.category.entity.Category;
import com.tailtales.backend.domain.category.repository.CategoryRepository;
import com.tailtales.backend.domain.category.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final AdminService adminService;
    private final CategoryRepository categoryRepository;

    @Override
    public Integer insertCategory(CategoryRequestDto categoryRequestDto, String adminAccessToken) {

        adminService.verifyToken(adminAccessToken).block();

        Category parentCategory = null;
        int depth = 0;

        Integer parentBcno = categoryRequestDto.getParentBcno();
        if (parentBcno != null) {
            parentCategory = categoryRepository.findById(parentBcno)
                    .orElse(null); // 부모 카테고리가 존재하지 않을 수도 있으므로 orElse(null) 처리
            if (parentCategory != null) {
                depth = parentCategory.getDepth() + 1;
            }
        }

        Category category = Category.builder()
                .name(categoryRequestDto.getName())
                .parent(parentCategory)
                .depth(depth)
                .isDeleted(false)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return savedCategory.getBcno();
    }

    @Override
    public List<CategoriesResponseDto> getCategoryList(String adminAccessToken) {

        adminService.verifyToken(adminAccessToken).block();

        List<Category> categories = categoryRepository.findAllNotDeletedOrderByDepthAndParentBcno();
        return categories.stream()
                .map(category -> CategoriesResponseDto.builder()
                        .bcno(category.getBcno())
                        .name(category.getName())
                        .parentBcno((category.getParent() != null) ? category.getParent().getBcno() : 0) // 또는 null 처리
                        .depth(category.getDepth())
                        .build())
                .collect(Collectors.toList());

    }

    @Override
    public Integer updateCategory(Integer bcno, CategoryUpdateRequestDto categoryUpdateRequestDto, String adminAccessToken) {

        adminService.verifyToken(adminAccessToken).block();

        Category existingCategory = categoryRepository.findByBcnoAndIsNotDeleted(bcno)
                .orElseThrow(() -> new IllegalArgumentException("삭제되었거나 존재하지 않는 카테고리 ID입니다."));

        Category.CategoryBuilder updatedCategoryBuilder = existingCategory.toBuilder()
                .name(categoryUpdateRequestDto.getName());

        if (categoryUpdateRequestDto.getParentBcno() != null) {
            // 새로운 부모 카테고리 조회 및 예외 처리
            Category parentCategory = categoryRepository.findById(categoryUpdateRequestDto.getParentBcno())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부모 카테고리 ID입니다."));
            updatedCategoryBuilder.parent(parentCategory)
                    .depth(parentCategory.getDepth() + 1);
        } else {
            updatedCategoryBuilder.parent(null)
                    .depth(0); // 최상위 카테고리로 변경
        }

        // 업데이트된 카테고리 저장
        Category updatedCategory = categoryRepository.save(updatedCategoryBuilder.build());

        // 수정된 카테고리 ID 반환
        return updatedCategory.getBcno();

    }

    @Override
    public Integer deleteCategory(Integer bcno, String adminAccessToken) {

        adminService.verifyToken(adminAccessToken).block();

        Category existingCategory = categoryRepository.findByBcnoAndIsNotDeleted(bcno)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 입니다. bcno: " + bcno));

        Category deletedCategory = existingCategory.toBuilder()
                .isDeleted(true)
                .build();

        categoryRepository.save(deletedCategory);

        return bcno;
    }

}
