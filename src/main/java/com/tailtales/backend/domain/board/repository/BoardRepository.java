package com.tailtales.backend.domain.board.repository;

import com.tailtales.backend.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 모든 글 조회
    @Query("SELECT b FROM Board b WHERE b.isDeleted = false ORDER BY b.createdAt DESC")
    Page<Board> findAllNotDeletedOrderByCreatedAtDesc(Pageable pageable);

    // 오래된 순 조회
    @Query("SELECT b FROM Board b WHERE b.isDeleted = false ORDER BY b.createdAt ASC")
    Page<Board> findAllNotDeletedOrderByCreatedAtAsc(Pageable pageable);

    // 조회수 순 조회
    @Query("SELECT b FROM Board b WHERE b.isDeleted = false ORDER BY b.viewCnt DESC")
    Page<Board> findAllNotDeletedOrderByViewCntDesc(Pageable pageable);

    // 카테고리 필터링 조회
    @Query("SELECT b FROM Board b JOIN b.categories c WHERE b.isDeleted = false AND c.bcno IN :categoryIds ORDER BY b.createdAt DESC")
    Page<Board> findAllNotDeletedByCategoriesOrderByCreatedAtDesc(@Param("categoryIds") List<Integer> categoryIds, Pageable pageable);

    // 카테고리 필터링 조회(오래된 순)
    @Query("SELECT b FROM Board b JOIN b.categories c WHERE b.isDeleted = false AND c.bcno IN :categoryIds ORDER BY b.createdAt ASC")
    Page<Board> findAllNotDeletedByCategoriesOrderByCreatedAtAsc(@Param("categoryIds") List<Integer> categoryIds, Pageable pageable);

    // 카테고리 필터링 조회(조회수 순)
    @Query("SELECT b FROM Board b JOIN b.categories c WHERE b.isDeleted = false AND c.bcno IN :categoryIds ORDER BY b.viewCnt DESC")
    Page<Board> findAllNotDeletedByCategoriesOrderByViewCntDesc(@Param("categoryIds") List<Integer> categoryIds, Pageable pageable);

    // 개별 글 조회
    @Query("SELECT b FROM Board b WHERE b.bno = :bno AND b.isDeleted = false")
    Optional<Board> findByBnoAndIsNotDeleted(@Param("bno") long bno);
}
