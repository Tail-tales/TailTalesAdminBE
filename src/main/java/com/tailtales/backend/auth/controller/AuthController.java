package com.tailtales.backend.auth.controller;

import com.tailtales.backend.auth.service.AuthService;
import com.tailtales.backend.domain.admin.dto.AdminLoginRequestDto;
import com.tailtales.backend.domain.admin.dto.AdminLoginResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Log4j2
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication")
public class AuthController {

    private final AuthService authService;

    // 관리자 로그인
    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDto> login(@RequestBody AdminLoginRequestDto requestDto) {
        AdminLoginResponseDto responseDto = authService.login(requestDto.getAdminId(), requestDto.getPassword());
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Unauthorized (인증되지 않음)
        }
    }

    // 관리자 비밀번호 찾기
    @PostMapping("/findPassword")
    public ResponseEntity<String> findPassword(@RequestParam String adminId) {
        try {
            authService.sendMail(adminId);
            return ResponseEntity.ok("새로운 비밀번호를 해당 관리자의 이메일로 발송했습니다.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("새로운 비밀번호 발송에 실패했습니다.");
        }
    }

}
