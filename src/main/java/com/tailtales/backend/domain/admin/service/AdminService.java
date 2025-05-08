package com.tailtales.backend.domain.admin.service;

import com.tailtales.backend.domain.admin.dto.AdminInsertRequestDto;
import com.tailtales.backend.domain.admin.dto.AdminResponseDto;
import com.tailtales.backend.domain.admin.dto.AdminUpdateRequestDto;
import reactor.core.publisher.Mono;

public interface AdminService {

    Mono<String> verifyToken(String adminAccessToken);

    Mono<String> insertAdmin(AdminInsertRequestDto requestDto);

    Mono<AdminResponseDto> getAdminInfo(String id, String adminAccessToken);

    Mono<String> updateAdminInfo(AdminUpdateRequestDto requestDto, String adminAccessToken);

    Mono<String> deleteAdmin(String id, String adminAccessToken);

}
