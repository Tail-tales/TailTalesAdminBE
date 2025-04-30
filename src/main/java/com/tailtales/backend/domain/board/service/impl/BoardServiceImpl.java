package com.tailtales.backend.domain.board.service.impl;

import com.tailtales.backend.domain.admin.entity.Admin;
import com.tailtales.backend.domain.admin.repository.AdminRepository;
import com.tailtales.backend.domain.board.dto.BoardResponseDto;
import com.tailtales.backend.domain.board.dto.BoardsResponseDto;
import com.tailtales.backend.domain.board.dto.PostRequestDto;
import com.tailtales.backend.domain.board.entity.Board;
import com.tailtales.backend.domain.board.repository.BoardRepository;
import com.tailtales.backend.domain.common.dto.PageRequestDto;
import com.tailtales.backend.domain.common.dto.PageResponseDto;
import com.tailtales.backend.domain.board.service.BoardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final AdminRepository adminRepository;
    private final BoardRepository boardRepository;

    @Override
    public PageResponseDto<BoardsResponseDto> getBoardList(PageRequestDto pageRequestDto) {

        Pageable pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize(), Sort.by("createdAt").descending());

        Page<Board> result = boardRepository.findAllNotDeletedOrderByCreatedAtDesc(pageable);

        return pageEntityToDto(pageRequestDto, result);
    }

    @Override
    public PageResponseDto<BoardsResponseDto> getBoardList(int categoryId, PageRequestDto pageRequestDto) {

        Pageable pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize(), Sort.by("createdAt").descending());

        Page<Board> result = boardRepository.findAllNotDeletedByCategoryOrderByCreatedAtDesc(categoryId, pageable);

        return pageEntityToDto(pageRequestDto, result);
    }

    private PageResponseDto<BoardsResponseDto> pageEntityToDto(PageRequestDto pageRequestDto, Page<Board> result) {
        List<BoardsResponseDto> dtoList = result.getContent().stream()
                .map(board -> BoardsResponseDto.builder()
                        .title(board.getTitle())
                        .name(board.getAdmin() != null ? board.getAdmin().getAdminId() : null)
                        .viewCnt(board.getViewCnt())
                        .createdAt(board.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return PageResponseDto.<BoardsResponseDto>withAll()
                .dtoList(dtoList)
                .pageRequestDto(pageRequestDto)
                .totalCount(result.getTotalElements())
                .build();
    }

    @Override
    public Optional<BoardResponseDto> getBoardInfo(long bno) {

        Optional<Board> boardInfo = boardRepository.findByBnoAndIsNotDeleted(bno);

        return boardInfo.map(board -> {
            board.increaseViewCnt(); // 조회수 증가
            return BoardResponseDto.builder()
                    .bno(board.getBno())
                    .title(board.getTitle())
                    .name(board.getAdmin() != null ? board.getAdmin().getAdminId() : null)
                    .content(board.getContent())
                    .viewCnt(board.getViewCnt())
                    .createdAt(board.getCreatedAt())
                    .build();
        });

    }

    @Override
    public Long insertBoard(PostRequestDto postRequestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Admin currentAdmin;

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            String adminId = ((UserDetails) authentication.getPrincipal()).getUsername();
            currentAdmin = adminRepository.findByAdminId(adminId)
                    .orElseThrow(() -> new RuntimeException("현재 로그인한 관리자 정보를 찾을 수 없습니다."));
        } else {
            throw new RuntimeException("인증된 관리자 정보를 찾을 수 없습니다.");
        }

        Board board = Board.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .admin(currentAdmin)
                .viewCnt(0)
                .isDeleted(false)
                .build();

        Board savedBoard = boardRepository.save(board);
        return savedBoard.getBno();

    }

}