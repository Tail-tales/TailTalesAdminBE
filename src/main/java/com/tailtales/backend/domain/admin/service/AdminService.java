package com.tailtales.backend.domain.admin.service;

import com.tailtales.backend.domain.admin.dto.AdminCreateRequestDto;

public interface AdminService {

    void insertAdmin(AdminCreateRequestDto adminCreateRequestDto);

    boolean isDuplicateAdminId(String adminId);

    boolean isDuplicateEmail(String email);


}
