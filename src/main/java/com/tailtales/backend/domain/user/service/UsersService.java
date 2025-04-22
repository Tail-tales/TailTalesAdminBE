package com.tailtales.backend.domain.user.service;

import com.tailtales.backend.domain.user.dto.UsersResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsersService {

    Mono<UsersResponseDto> getUserByProviderId(String provider, String providerId);
    Flux<UsersResponseDto> getUsers();

}
