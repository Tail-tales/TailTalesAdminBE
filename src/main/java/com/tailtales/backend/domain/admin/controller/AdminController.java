package com.tailtales.backend.domain.admin.controller;

import com.tailtales.backend.domain.admin.dto.AdminCreateRequestDto;
import com.tailtales.backend.domain.admin.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
@Tag(name = "Admin", description = "Admin API")
public class AdminController {

    private final AdminService adminService;

    // 관리자 등록
    @PostMapping
    public ResponseEntity<String> insertAdmin(@RequestBody @Valid AdminCreateRequestDto adminCreateRequestDto) {
        adminService.insertAdmin(adminCreateRequestDto);
        return ResponseEntity.ok("관리자 등록이 완료되었습니다.");
    }

    // 관리자 아이디 중복 체크
    @GetMapping("/check-id-duplication")
    public ResponseEntity<Boolean> checkDuplicateAdminId(@RequestParam(name = "adminId") String adminId) {
        boolean isDuplicate = adminService.isDuplicateAdminId(adminId);
        return ResponseEntity.ok(isDuplicate);
    }

    // 이메일 중복 체크
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkDuplicateEmail(@RequestParam(name = "email") String email) {
        boolean isDuplicate = adminService.isDuplicateEmail(email);
        return ResponseEntity.ok(isDuplicate);
    }
}