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

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "아이디를 입력해주세요.")
    private String adminId;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,20}$",
            message = "비밀번호는 8~20자, 영문/숫자/특수문자를 포함해야 합니다."
    )
    private String password;

    @Pattern(regexp = "^01[0-9]\\d{7,8}$", message = "올바른 휴대폰 번호를 입력해주세요.")
    private String contact;

    @Email
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;

}
