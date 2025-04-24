package com.tailtales.backend.auth.service;

import com.tailtales.backend.auth.dto.MailResponseDto;
import com.tailtales.backend.domain.admin.dto.AdminLoginResponseDto;
import com.tailtales.backend.domain.admin.entity.Admin;
import com.tailtales.backend.domain.admin.repository.AdminRepository;
import com.tailtales.backend.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final SimpleMailMessageService simpleMailMessageService;
    private final AdminRepository adminRepository;
    private static final String ADMIN_ROLE = "ADMIN";

    public AdminLoginResponseDto login(String adminId, String password) {

        // 사용자 인증 (AuthenticationManager 사용)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminId, password)
        );

        // 인증 성공 시 JWT 토큰 생성
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(adminId);
            String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername(), Map.of("roles", List.of(ADMIN_ROLE)));
            String refreshToken = jwtUtil.generateRefreshToken();

            long accessTokenExpiresIn = jwtUtil.getExpirationTimeFromAccessToken(accessToken);
            long refreshTokenExpiresIn = jwtUtil.getExpirationTimeFromRefreshToken(refreshToken);

            return AdminLoginResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(accessTokenExpiresIn / 1000) // 초 단위로 변환
                    .refreshExpiresIn(refreshTokenExpiresIn / 1000) // 초 단위로 변환
                    .adminId(adminId)
                    .build();
        }

        return null; // 인증 실패
    }

    public void sendMail(String adminId) {
        Admin admin = adminRepository.findByAdminId(adminId)
                .orElseThrow(() -> new NoSuchElementException("해당 아이디의 관리자를 찾을 수 없습니다."));

        // 1. 임시 비밀번호 생성
        String newPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 2. DB에 새로운 비밀번호 저장
        Admin updatedAdmin = admin.toBuilder()
                .password(encodedPassword)
                .build();
        adminRepository.save(updatedAdmin);

        // 3. MailResponseDto 생성
        MailResponseDto mailResponseDto = MailResponseDto.builder()
                .to(admin.getEmail())
                .title("새로운 비밀번호 안내")
                .content("새로운 비밀번호는 다음과 같습니다: " + newPassword + "\n로그인 후 반드시 비밀번호를 변경해주세요.")
                .build();

        // 4. 이메일 발송
        simpleMailMessageService.sendEmail(mailResponseDto.getTo(), mailResponseDto.getTitle(), mailResponseDto.getContent());
        
    }

    private String generateRandomPassword() {
        // UUID를 사용하여 임시 비밀번호 생성 (길이 조절 가능)
        return UUID.randomUUID().toString().substring(0, 12);
    }

}
