package com.tailtales.backend.domain.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUpdateRequestDto {

    @Size(max = 50)
    @NotBlank
    private String name;

    @Size(max = 20)
    @Pattern(regexp = "^01[0-9]\\d{7,8}$")
    private String contact;

    @Size(max = 100)
    @Email
    @NotBlank
    private String email;

}
