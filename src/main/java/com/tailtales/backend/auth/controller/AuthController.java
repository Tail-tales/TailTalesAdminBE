package com.tailtales.backend.auth.controller;

import com.tailtales.backend.auth.service.AuthService;
import com.tailtales.backend.domain.admin.dto.AdminLoginRequestDto;
import com.tailtales.backend.domain.admin.dto.AdminLoginResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDto> login(@RequestBody AdminLoginRequestDto requestDto) {
        AdminLoginResponseDto responseDto = authService.login(requestDto.getAdminId(), requestDto.getPassword());
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Unauthorized (인증되지 않음)
        }
    }

}
