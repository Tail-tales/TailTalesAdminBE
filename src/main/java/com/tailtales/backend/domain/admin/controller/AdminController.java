package com.tailtales.backend.domain.admin.controller;

import com.tailtales.backend.domain.admin.dto.AdminCreateRequestDto;
import com.tailtales.backend.domain.admin.dto.AdminUpdateRequestDto;
import com.tailtales.backend.domain.admin.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
@Tag(name = "Admin", description = "Admin API")
public class AdminController {

    private final AdminService adminService;

    // 관리자 회원가입
    @PostMapping
    public ResponseEntity<String> insertAdmin(@RequestBody @Valid AdminCreateRequestDto adminCreateRequestDto) {
        adminService.insertAdmin(adminCreateRequestDto);
        return ResponseEntity.ok("관리자 등록이 완료되었습니다.");
    }

    // 관리자 아이디 중복 체크
    @GetMapping("/exists/{adminId}")
    public ResponseEntity<Boolean> checkDuplicateAdminId(@PathVariable(name = "adminId") String adminId) {
        boolean isDuplicate = adminService.isDuplicateAdminId(adminId);
        return ResponseEntity.ok(isDuplicate);
    }

    // 이메일 중복 체크
    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> checkDuplicateEmail(@PathVariable(name = "email") String email) {
        boolean isDuplicate = adminService.isDuplicateEmail(email);
        return ResponseEntity.ok(isDuplicate);
    }

    // 관리자 개인 정보 수정
    @PutMapping("/me")
    public ResponseEntity<String> updateAdmin(@RequestBody @Valid AdminUpdateRequestDto adminUpdateRequestDto,
                                              @AuthenticationPrincipal UserDetails userDetails) {

        String adminId = userDetails.getUsername();

        adminService.updateAdminInfo(adminId, adminUpdateRequestDto);
        return ResponseEntity.ok("관리자 정보 수정이 완료되었습니다.");
    }

    // 관리자 계정 삭제
    @DeleteMapping("/{adminId}")
    public ResponseEntity<String> deleteAdmin(@PathVariable String adminId) {
        adminService.deleteAdmin(adminId);
        return ResponseEntity.ok("관리자 계정이 삭제되었습니다.");
    }

}