package com.tailtales.backend.domain.admin.controller;

import com.tailtales.backend.domain.admin.dto.AdminInsertRequestDto;
import com.tailtales.backend.domain.admin.dto.AdminResponseDto;
import com.tailtales.backend.domain.admin.dto.AdminUpdateRequestDto;
import com.tailtales.backend.domain.admin.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Admin API")
public class AdminController {

    private final AdminService adminService;

    // 관리자 토큰 검증
    @GetMapping("/verify")
    public Mono<ResponseEntity<String>> verifyToken(@RequestHeader("Authorization") String adminAccessToken) {
        return adminService.verifyToken(adminAccessToken)
                .map(ResponseEntity::ok);

    }

    // 관리자 회원가입 요청
    @PostMapping
    public Mono<ResponseEntity<String>> signupAdmin(@RequestBody AdminInsertRequestDto requestDto) {
        return adminService.insertAdmin(requestDto)
                .map(ResponseEntity::ok);
    }

    // 특정 관리자 정보 조회 요청
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AdminResponseDto>> getAdminInfo(@PathVariable(name = "id") String id,
                                                               @RequestHeader("Authorization") String adminAccessToken) {
        return adminService.getAdminInfo(id, adminAccessToken)
                .map(ResponseEntity::ok);
    }

    // 특정 관리자 정보 수정 요청
    @PutMapping("/me")
    public Mono<ResponseEntity<String>> updateAdminInfo(@RequestBody AdminUpdateRequestDto requestDto,
                                                        @RequestHeader("Authorization") String adminAccessToken) {
        return adminService.updateAdminInfo(requestDto, adminAccessToken)
                .map(ResponseEntity::ok);
    }

    // 특정 관리자 삭제 요청
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteAdmin(@PathVariable(name = "id") String id,
                                                    @RequestHeader("Authorization") String adminAccessToken) {
        return adminService.deleteAdmin(id, adminAccessToken)
                .map(ResponseEntity::ok);
    }

}