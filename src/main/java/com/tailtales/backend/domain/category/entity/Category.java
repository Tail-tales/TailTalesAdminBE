package com.tailtales.backend.domain.category.entity;

import com.tailtales.backend.domain.board.entity.Board;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "board_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bcno;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_bcno")
    private Category parent;

    private int depth;

    @CreatedDate
    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt= LocalDateTime.now();

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToMany(mappedBy = "categories")
    private List<Board> boards = new ArrayList<>();

}
