package com.tailtales.backend.domain.user.service.impl;

import com.tailtales.backend.domain.user.dto.UsersResponseDto;
import com.tailtales.backend.domain.user.service.UsersService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    private final WebClient webClient;

    public UsersServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<UsersResponseDto> getUserByProviderId(String provider, String providerId) {

        return webClient.get()
                .uri("/api/users/{provider}/{providerId}", provider, providerId)
                .retrieve()
                .bodyToMono(UsersResponseDto.class);

    }

    public Flux<UsersResponseDto> getUsers() {

        return webClient.get()
                .uri("/api/users")
                .retrieve()
                .bodyToFlux(UsersResponseDto.class);

    }

}
