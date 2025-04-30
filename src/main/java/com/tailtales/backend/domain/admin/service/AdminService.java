package com.tailtales.backend.domain.admin.service;

import com.tailtales.backend.domain.admin.dto.AdminCreateRequestDto;
import com.tailtales.backend.domain.admin.dto.AdminResponseDto;
import com.tailtales.backend.domain.admin.dto.AdminUpdateRequestDto;

public interface AdminService {

    void insertAdmin(AdminCreateRequestDto adminCreateRequestDto);

    boolean isDuplicateAdminId(String adminId);

    boolean isDuplicateEmail(String email);

    AdminResponseDto getAdminInfo(String adminId);

    void updateAdminInfo(String adminId, AdminUpdateRequestDto adminUpdateRequestDto);

    void deleteAdmin(String adminId);

}
