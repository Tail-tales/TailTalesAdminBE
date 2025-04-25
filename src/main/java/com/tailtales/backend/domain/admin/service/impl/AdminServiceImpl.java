package com.tailtales.backend.domain.admin.service.impl;

import com.tailtales.backend.domain.admin.dto.AdminCreateRequestDto;
import com.tailtales.backend.domain.admin.dto.AdminUpdateRequestDto;
import com.tailtales.backend.domain.admin.entity.Admin;
import com.tailtales.backend.domain.admin.repository.AdminRepository;
import com.tailtales.backend.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void insertAdmin(AdminCreateRequestDto dto) {

        if (adminRepository.existsByAdminId(dto.getAdminId())) {

            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");

        }

        if (adminRepository.existsByEmail(dto.getEmail())) {

            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");

        }

        Admin admin = Admin.builder()
                .name(dto.getName())
                .adminId(dto.getAdminId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .contact(dto.getContact())
                .email(dto.getEmail())
                .isDeleted(false)
                .build();

        adminRepository.save(admin);

    }

    @Override
    public boolean isDuplicateAdminId(String adminId) {
        return adminRepository.existsByAdminId(adminId);
    }

    @Override
    public void updateAdminInfo(String adminId, AdminUpdateRequestDto dto) {

        Admin admin = adminRepository.findByAdminId(adminId)
                .orElseThrow(() -> new IllegalArgumentException("해당 관리자를 찾을 수 없습니다."));

        Admin.AdminBuilder adminBuilder = admin.toBuilder();

        if (dto.getName() != null) {
            adminBuilder.name(dto.getName());
        }
        if (dto.getPassword() != null) {
            adminBuilder.password(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getContact() != null) {
            adminBuilder.contact(dto.getContact());
        }
        if (dto.getEmail() != null) {
            adminBuilder.email(dto.getEmail());
        }

        adminRepository.save(adminBuilder.build());

    }

    @Override
    public void deleteAdmin(String adminId) {

        Admin admin = adminRepository.findByAdminId(adminId)
                .orElseThrow(() -> new IllegalArgumentException("해당 관리자를 찾을 수 없습니다."));

        admin = admin.toBuilder()
                .isDeleted(true)
                .build();
        adminRepository.save(admin);

    }
}
