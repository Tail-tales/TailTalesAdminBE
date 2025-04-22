package com.tailtales.backend.domain.user.controller;

import com.tailtales.backend.domain.user.dto.UsersResponseDto;
import com.tailtales.backend.domain.user.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/{provider}/{providerId}")
    public Mono<ResponseEntity<UsersResponseDto>> getUserByProviderId(
            @PathVariable String provider,
            @PathVariable String providerId) {

        return usersService.getUserByProviderId(provider, providerId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<UsersResponseDto>>> getAllUsers() {
        return Mono.just(ResponseEntity.ok(usersService.getUsers()));
    }

}
