package com.tailtales.backend.domain.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String adminId;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,20}$")
    private String password;

    @Pattern(regexp = "^01[0-9]\\d{7,8}$")
    private String contact;

    @Email
    @NotBlank
    private String email;

}
