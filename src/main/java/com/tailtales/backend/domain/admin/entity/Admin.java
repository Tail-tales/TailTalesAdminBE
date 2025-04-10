package com.tailtales.backend.domain.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ano;

    @Column(length = 50)
    private String name;

    @Column(name = "admin_id", unique = true, length = 50)
    private String adminId;

    @Column(length = 100)
    private String password;

    @Column(length = 20)
    private String contact;

    @Column(unique = true, length = 100)
    private String email;

    @CreatedDate
    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_deleted")
    private boolean isDeleted;

}