package com.tailtales.backend.domain.admin.repository;

import com.tailtales.backend.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    boolean existsByAdminId(String adminId);

    boolean existsByEmail(String email);

}