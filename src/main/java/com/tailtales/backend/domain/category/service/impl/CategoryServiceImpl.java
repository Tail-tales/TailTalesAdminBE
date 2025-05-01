package com.tailtales.backend.domain.category.service.impl;

import com.tailtales.backend.domain.category.dto.CategoriesResponseDto;
import com.tailtales.backend.domain.category.dto.CategoryRequestDto;
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

    private final CategoryRepository categoryRepository;

    @Override
    public Integer insertCategory(CategoryRequestDto categoryRequestDto) {
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
    public List<CategoriesResponseDto> getCategoryList() {

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

}
