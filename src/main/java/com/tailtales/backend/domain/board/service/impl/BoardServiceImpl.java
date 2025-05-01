package com.tailtales.backend.domain.board.service.impl;

import com.tailtales.backend.domain.admin.entity.Admin;
import com.tailtales.backend.domain.admin.repository.AdminRepository;
import com.tailtales.backend.domain.board.dto.BoardResponseDto;
import com.tailtales.backend.domain.board.dto.BoardUpdateRequestDto;
import com.tailtales.backend.domain.board.dto.BoardsResponseDto;
import com.tailtales.backend.domain.board.dto.BoardRequestDto;
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

import java.time.LocalDateTime;
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

    // 관리자 확인 로직
    private Admin getAuthenticatedAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }

        String currentAdminId = userDetails.getUsername();
        return adminRepository.findByAdminId(currentAdminId)
                .orElseThrow(() -> new RuntimeException("현재 로그인한 관리자 정보를 찾을 수 없습니다."));
    }

    @Override
    public Long insertBoard(BoardRequestDto boardRequestDto) {

        Admin currentAdmin = getAuthenticatedAdmin();

        Board board = Board.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .admin(currentAdmin)
                .viewCnt(0)
                .isDeleted(false)
                .build();

        Board savedBoard = boardRepository.save(board);
        return savedBoard.getBno();

    }

    @Override
    public Optional<BoardResponseDto> updateBoard(BoardUpdateRequestDto boardUpdateRequestDto) {

        Admin currentAdmin = getAuthenticatedAdmin();

        // 수정할 게시글 조회
        Board existingBoard = boardRepository.findByBnoAndIsNotDeleted(boardUpdateRequestDto.getBno())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. bno: " + boardUpdateRequestDto.getBno()));

        if (!existingBoard.getAdmin().getAdminId().equals(currentAdmin.getAdminId())) {
            throw new IllegalArgumentException("자신이 작성한 게시글만 수정할 수 있습니다.");
        }

        // 기존 게시글 정보를 바탕으로 새로운 Board 객체 생성 (toBuilder 사용)
        Board updatedBoard = existingBoard.toBuilder()
                .bno(existingBoard.getBno()) // 기존 bno 명시적으로 설정
                .title(boardUpdateRequestDto.getTitle())
                .content(boardUpdateRequestDto.getContent())
                .updatedAt(LocalDateTime.now()) // 수정 시간 업데이트
                .build();

        // 업데이트된 게시글 저장
        Board savedBoard = boardRepository.save(updatedBoard);

        // 수정된 게시글 정보를 담은 BoardResponseDto 생성 및 반환
        BoardResponseDto responseDto = BoardResponseDto.builder()
                .bno(savedBoard.getBno())
                .title(savedBoard.getTitle())
                .name(existingBoard.getAdmin().getAdminId()) // 기존 작성자 유지
                .content(savedBoard.getContent())
                .viewCnt(savedBoard.getViewCnt())
                .createdAt(savedBoard.getCreatedAt())
                .updatedAt(savedBoard.getUpdatedAt())
                .build();

        return Optional.of(responseDto);

    }

}