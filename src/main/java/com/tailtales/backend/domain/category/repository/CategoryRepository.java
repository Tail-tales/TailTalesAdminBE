package com.tailtales.backend.domain.category.repository;

import com.tailtales.backend.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // 카테고리 조회
    @Query("SELECT c FROM Category c WHERE c.isDeleted = false ORDER BY c.depth ASC, c.parent.bcno ASC NULLS FIRST")
    List<Category> findAllNotDeletedOrderByDepthAndParentBcno();

}
