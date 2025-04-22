package com.tailtales.backend.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsersResponseDto {

    private int uno;

    @NotBlank
    private String provider;

    @NotBlank
    private String providerId;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String level;

    private String contact;

    @NotBlank
    private LocalDateTime createdAt;

    @NotBlank
    private String role;

    private String imgUrl;

}
