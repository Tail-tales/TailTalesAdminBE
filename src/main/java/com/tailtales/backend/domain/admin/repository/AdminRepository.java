package com.tailtales.backend.domain.admin.repository;

import com.tailtales.backend.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    // 아이디 중복 체크
    boolean existsByAdminId(String adminId);

    // 이메일 중복 체크
    boolean existsByEmail(String email);

    // 아이디 조회
    Optional<Admin> findByAdminId(String adminId);

}