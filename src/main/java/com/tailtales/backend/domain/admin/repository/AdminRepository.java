package com.tailtales.backend.domain.admin.repository;

import com.tailtales.backend.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    // 아이디 중복 체크
    @Query("SELECT COUNT(a) > 0 FROM Admin a WHERE a.adminId = :adminId AND a.isDeleted = false")
    boolean existsByAdminId(@Param("adminId") String adminId);

    // 이메일 중복 체크
    @Query("SELECT COUNT(a) > 0 FROM Admin a WHERE a.email = :email AND a.isDeleted = false")
    boolean existsByEmail(@Param("email") String email);

    // 아이디 조회
    @Query("SELECT a FROM Admin a WHERE a.adminId = :adminId AND a.isDeleted = false")
    Optional<Admin> findByAdminId(@Param("adminId") String adminId);

}