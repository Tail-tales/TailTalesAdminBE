package com.tailtales.backend.domain.user.controller;

import com.tailtales.backend.domain.user.dto.UsersResponseDto;
import com.tailtales.backend.domain.user.service.UsersService;
import com.tailtales.backend.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UsersController {

    private final UsersService usersService;
    private final JwtUtil jwtUtil;

    // 토큰값 유효성 검증
    private boolean isValid(String token) {
        return token != null && jwtUtil.validateAccessToken(token);
    }

    // 유저 개별 조회
    @GetMapping("/{provider}/{providerId}")
    public Mono<ResponseEntity<UsersResponseDto>> getUserByProviderId(
            @PathVariable(name = "provider") String provider,
            @PathVariable(name = "providerId") String providerId,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = jwtUtil.extractTokenFromHeader(authorizationHeader);

        if (isValid(token)) {
            return usersService.getUserByProviderId(provider, providerId, token)
                    .map(ResponseEntity::ok);
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    // 모든 유저 조회
    @GetMapping
    public Mono<ResponseEntity<Flux<UsersResponseDto>>> getAllUsers(
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = jwtUtil.extractTokenFromHeader(authorizationHeader);

        if (isValid(token)) {
            return Mono.just(ResponseEntity.ok(usersService.getUsers(token)));
        }

        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

    }

    // 유저 삭제
    @DeleteMapping("/{provider}/{providerId}")
    public Mono<ResponseEntity<Void>> deleteUserByProviderId(
            @PathVariable(name = "provider") String provider,
            @PathVariable(name = "providerId") String providerId,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = jwtUtil.extractTokenFromHeader(authorizationHeader);

        if (isValid(token)) {
            return usersService.deleteUserByProviderId(provider, providerId, token)
                    .then(Mono.just(ResponseEntity.noContent().build()));
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
        }

    }


}
