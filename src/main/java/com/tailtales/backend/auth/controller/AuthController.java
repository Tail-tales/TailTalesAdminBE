package com.tailtales.backend.auth.controller;

import com.tailtales.backend.auth.service.AuthService;
import com.tailtales.backend.domain.admin.dto.AdminLoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AdminLoginRequestDto requestDto) {
        String token = authService.login(requestDto.getAdminId(), requestDto.getPassword());
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("인증 실패"); // Unauthorized (인증되지 않음)
        }
    }

}
