package com.tailtales.backend.domain.category.repository;

import com.tailtales.backend.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // 카테고리 조회
    @Query("SELECT c FROM Category c WHERE c.isDeleted = false ORDER BY c.depth ASC, c.parent.bcno ASC NULLS FIRST")
    List<Category> findAllNotDeletedOrderByDepthAndParentBcno();

    // 개별 카테고리 조회(bno 값 기준)
    @Query("SELECT c FROM Category c WHERE c.bcno = :bcno AND c.isDeleted = false")
    Optional<Category> findByBcnoAndIsNotDeleted(@Param("bcno") Integer bcno);

    // 개별 카테고리 조회(name 값 기준)
    @Query("SELECT c FROM Category c WHERE  c.name = :name AND c.isDeleted = false")
    Optional<Category> findByNameAndIsDeletedFalse(String name);

}
