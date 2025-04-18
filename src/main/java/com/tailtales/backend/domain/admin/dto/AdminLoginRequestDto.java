package com.tailtales.backend.domain.admin.dto;

import lombok.Data;

@Data
public class AdminLoginRequestDto {

    private String adminId;

    private String password;

}
