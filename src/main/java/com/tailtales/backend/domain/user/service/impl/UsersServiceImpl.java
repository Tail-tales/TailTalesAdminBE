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

    @Override
    public Mono<UsersResponseDto> getUserByProviderId(String provider, String providerId, String token) {

        return webClient.get()
                .uri("/api/users/{provider}/{providerId}", provider, providerId)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(UsersResponseDto.class);

    }

    @Override
    public Flux<UsersResponseDto> getUsers(String token) {

        return webClient.get()
                .uri("/api/users")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToFlux(UsersResponseDto.class);

    }

    @Override
    public Mono<Void> deleteUserByProviderId(String provider, String providerId, String token) {
        return webClient.delete()
                .uri("/api/users/{provider}/{providerId}", provider, providerId)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(Void.class);
    }

}
