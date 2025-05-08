package com.tailtales.backend.domain.admin.service.impl;

import com.tailtales.backend.domain.admin.dto.AdminInsertRequestDto;
import com.tailtales.backend.domain.admin.dto.AdminResponseDto;
import com.tailtales.backend.domain.admin.dto.AdminUpdateRequestDto;
import com.tailtales.backend.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Qualifier("authWebClient")
    private final WebClient webClient;

    // 토큰 검증 요청
    public Mono<String> verifyToken(String adminAccessToken) {

        return webClient.get()
                .uri("/auth/verify")
                .headers(headers -> headers.set("Authorization", adminAccessToken)) // 필요하다면 관리자 서버의 인증 토큰을 전달
                .retrieve()
                .bodyToMono(String.class);

    }

    // 관리자 회원가입 요청
    public Mono<String> insertAdmin(AdminInsertRequestDto requestDto) {
        return webClient.post()
                .uri("/api/members") // Auth 서버의 관리자 회원가입 API 엔드포인트
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(String.class); // 성공 메시지 등 응답 타입에 따라 변경
    }

    // 특정 관리자 정보 조회 요청
    public Mono<AdminResponseDto> getAdminInfo(String id, String adminAccessToken) {
        return webClient.get()
                .uri("/api/members/{id}", id) // Auth 서버의 관리자 정보 조회 API 엔드포인트
                .headers(headers -> headers.set("Authorization", adminAccessToken)) // 필요하다면 관리자 서버의 인증 토큰을 전달
                .retrieve()
                .bodyToMono(AdminResponseDto.class);
    }

    // 특정 관리자 정보 수정 요청
    public Mono<String> updateAdminInfo(AdminUpdateRequestDto requestDto, String adminAccessToken) {
        return webClient.put()
                .uri("/api/members/me") // Auth 서버의 관리자 정보 수정 API 엔드포인트
                .headers(headers -> headers.set("Authorization", adminAccessToken)) // 필요하다면 관리자 서버의 인증 토큰을 전달
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(String.class); // 성공 메시지 등 응답 타입에 따라 변경
    }

    // 특정 관리자 삭제 요청
    public Mono<String> deleteAdmin(String id, String adminAccessToken) {
        return webClient.delete()
                .uri("/api/members/{id}", id) // Auth 서버의 관리자 삭제 API 엔드포인트
                .headers(headers -> headers.set("Authorization", adminAccessToken)) // 필요하다면 관리자 서버의 인증 토큰을 전달
                .retrieve()
                .bodyToMono(String.class); // 성공 메시지 등 응답 타입에 따라 변경
    }

}
