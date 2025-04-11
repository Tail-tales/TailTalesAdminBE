package com.tailtales.backend.auth.service;

import com.tailtales.backend.domain.admin.entity.Admin;
import com.tailtales.backend.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder; // PasswordEncoder만 주입

    @Override
    public UserDetails loadUserByUsername(String adminId) throws UsernameNotFoundException {
        Optional<Admin> adminOptional = adminRepository.findByAdminId(adminId);
        if (adminOptional.isEmpty()) {
            throw new UsernameNotFoundException(adminId + "와 같은 아이디를 가진 관리자가 없습니다.");
        }
        Admin admin = adminOptional.get();

        return new User(admin.getAdminId(), admin.getPassword(), Collections.emptyList());
    }
}