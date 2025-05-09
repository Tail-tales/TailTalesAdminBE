package com.tailtales.backend.domain.board.service.impl;

import com.tailtales.backend.domain.admin.service.AdminService;
import com.tailtales.backend.domain.board.dto.BoardResponseDto;
import com.tailtales.backend.domain.board.dto.BoardUpdateRequestDto;
import com.tailtales.backend.domain.board.dto.BoardsResponseDto;
import com.tailtales.backend.domain.board.dto.BoardRequestDto;
import com.tailtales.backend.domain.board.entity.Board;
import com.tailtales.backend.domain.board.repository.BoardRepository;
import com.tailtales.backend.domain.category.entity.Category;
import com.tailtales.backend.domain.category.repository.CategoryRepository;
import com.tailtales.backend.domain.common.dto.PageRequestDto;
import com.tailtales.backend.domain.common.dto.PageResponseDto;
import com.tailtales.backend.domain.board.service.BoardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final AdminService adminService;
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public PageResponseDto<BoardsResponseDto> getBoardList(String sort, PageRequestDto pageRequestDto, String adminAccessToken) {

        adminService.verifyToken(adminAccessToken).block();

        Pageable pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize(), Sort.by("createdAt").descending());

        Page<Board> result = boardRepository.findAllNotDeletedOrderByCreatedAtDesc(pageable);

        // 정렬 방식 설정
        Sort sortCondition = Sort.by("createdAt").descending(); // 기본은 최신순

        if (sort != null) {
            if (sort.equalsIgnoreCase("oldest")) {
                sortCondition = Sort.by("createdAt").ascending();
            } else if (sort.equalsIgnoreCase("views")) {
                sortCondition = Sort.by("viewCnt").descending();
            }
            Pageable updatedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortCondition);

            if (sort.equalsIgnoreCase("oldest")) {
                result = boardRepository.findAllNotDeletedOrderByCreatedAtAsc(updatedPageable);
            } else if (sort.equalsIgnoreCase("views")) {
                result = boardRepository.findAllNotDeletedOrderByViewCntDesc(updatedPageable);
            }

        }

        return pageEntityToDto(pageRequestDto, result);
    }

    @Override
    public PageResponseDto<BoardsResponseDto> getBoardList(String sort, List<Integer> categoryIds, PageRequestDto pageRequestDto, String adminAccessToken) {

        adminService.verifyToken(adminAccessToken).block();

        Pageable pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize(), Sort.by("createdAt").descending());

        // 정렬 방식 설정
        Sort sortCondition = Sort.by("createdAt").descending();
        if (sort != null) {
            if (sort.equalsIgnoreCase("oldest")) {
                sortCondition = Sort.by("createdAt").ascending();
            } else if (sort.equalsIgnoreCase("views")) {
                sortCondition = Sort.by("viewCnt").descending();
            }
        }

        Pageable updatedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortCondition);

        Page<Board> result;

        if (categoryIds != null && (!categoryIds.isEmpty())) {
            if (sort != null) {
                if (sort.equalsIgnoreCase("oldest")) {
                    result = boardRepository.findAllNotDeletedByCategoriesOrderByCreatedAtAsc(categoryIds, updatedPageable);
                } else if (sort.equalsIgnoreCase("views")) {
                    result = boardRepository.findAllNotDeletedByCategoriesOrderByViewCntDesc(categoryIds, updatedPageable);
                } else { // 기본 정렬: 최신순
                    result = boardRepository.findAllNotDeletedByCategoriesOrderByCreatedAtDesc(categoryIds, updatedPageable);
                }
            } else { // sort 파라미터가 없는 경우 기본 최신순 정렬
                result = boardRepository.findAllNotDeletedByCategoriesOrderByCreatedAtDesc(categoryIds, updatedPageable);
            }
        } else {
            result = boardRepository.findAllNotDeletedOrderByCreatedAtDesc(updatedPageable);
        }

        return pageEntityToDto(pageRequestDto, result);
    }

    private PageResponseDto<BoardsResponseDto> pageEntityToDto(PageRequestDto pageRequestDto, Page<Board> result) {
        List<BoardsResponseDto> dtoList = result.getContent().stream()
                .map(board -> {
                    List<String> categoryNames = board.getCategories().stream()
                            .map(Category::getName)
                            .toList();

                    return BoardsResponseDto.builder()
                            .title(board.getTitle())
                            .name(board.getMemberId())
                            .viewCnt(board.getViewCnt())
                            .createdAt(board.getCreatedAt())
                            .categories(categoryNames)
                            .build();
                })
                .collect(Collectors.toList());

        return PageResponseDto.<BoardsResponseDto>withAll()
                .dtoList(dtoList)
                .pageRequestDto(pageRequestDto)
                .totalCount(result.getTotalElements())
                .build();
    }

    @Override
    public Optional<BoardResponseDto> getBoardInfo(long bno, String adminAccessToken) {

        adminService.verifyToken(adminAccessToken).block();

        Optional<Board> boardInfo = boardRepository.findByBnoAndIsNotDeleted(bno);

        return boardInfo.map(board -> {
            board.increaseViewCnt(); // 조회수 증가
            boardRepository.save(board); // 조회수 업데이트

            List<String> categoryNames = board.getCategories().stream()
                    .map(Category::getName)
                    .toList();

            return BoardResponseDto.builder()
                    .bno(board.getBno())
                    .title(board.getTitle())
                    .name(board.getMemberId())
                    .content(board.getContent())
                    .viewCnt(board.getViewCnt())
                    .createdAt(board.getCreatedAt())
                    .categories(categoryNames)
                    .build();
        });

    }

    @Override
    public Long insertBoard(BoardRequestDto boardRequestDto, String adminAccessToken) {

        String memberId = adminService.verifyToken(adminAccessToken)
                .block();

        Board board = Board.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .memberId(memberId)
                .viewCnt(0)
                .isDeleted(false)
                .build();

        List<Category> categories = new ArrayList<>();
        if (boardRequestDto.getCategories() != null && !boardRequestDto.getCategories().isEmpty()) {
            for (Integer bcno : boardRequestDto.getCategories()) {
                // 해당 bcno의 삭제되지 않은 카테고리가 존재하는지 조회
                Category category = categoryRepository.findByBcnoAndIsNotDeleted(bcno)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다: " + bcno));
                categories.add(category);
            }
        }
        board.getCategories().addAll(categories); // 게시글과 카테고리 연결

        Board savedBoard = boardRepository.save(board);
        return savedBoard.getBno();

    }

    @Override
    public Optional<BoardResponseDto> updateBoard(BoardUpdateRequestDto boardUpdateRequestDto, String adminAccessToken) {

        String memberId = adminService.verifyToken(adminAccessToken)
                .block();

        // 수정할 게시글 조회
        Board existingBoard = boardRepository.findByBnoAndIsNotDeleted(boardUpdateRequestDto.getBno())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. bno: " + boardUpdateRequestDto.getBno()));

        if (!existingBoard.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("자신이 작성한 게시글만 수정할 수 있습니다.");
        }

        // 업데이트할 카테고리 목록 생성
        List<Category> updatedCategories = new ArrayList<>();
        if (boardUpdateRequestDto.getCategories() != null && !boardUpdateRequestDto.getCategories().isEmpty()) {
            for (Integer bcno : boardUpdateRequestDto.getCategories()) {
                Category category = categoryRepository.findByBcnoAndIsNotDeleted(bcno)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다: " + bcno));
                updatedCategories.add(category);
            }
        }

        // 기존 게시글 정보를 기반으로 업데이트된 Board 객체 생성
        Board updatedBoard = existingBoard.toBuilder()
                .title(boardUpdateRequestDto.getTitle())
                .content(boardUpdateRequestDto.getContent())
                .updatedAt(LocalDateTime.now())
                .categories(updatedCategories)
                .build();

        // 업데이트된 게시글 저장
        Board savedBoard = boardRepository.save(updatedBoard);

        List<String> savedCategoryNames = savedBoard.getCategories().stream()
                .map(Category::getName)
                .toList();

        // 수정된 게시글 정보를 담은 BoardResponseDto 생성 및 반환
        BoardResponseDto responseDto = BoardResponseDto.builder()
                .bno(savedBoard.getBno())
                .title(savedBoard.getTitle())
                .name(savedBoard.getMemberId())
                .content(savedBoard.getContent())
                .viewCnt(savedBoard.getViewCnt())
                .createdAt(savedBoard.getCreatedAt())
                .updatedAt(savedBoard.getUpdatedAt())
                .categories(savedCategoryNames)
                .build();

        return Optional.of(responseDto);

    }

    @Override
    public void deleteBoard(long bno, String adminAccessToken) {

        String memberId = adminService.verifyToken(adminAccessToken)
                .block();

        // 삭제할 게시글 조회
        Board existingBoard = boardRepository.findByBnoAndIsNotDeleted(bno)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. bno: " + bno));

        if (!existingBoard.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("자신이 작성한 게시글만 삭제할 수 있습니다.");
        }

        Board deletedBoard = existingBoard.toBuilder()
                .isDeleted(true)
                .build();

        boardRepository.save(deletedBoard);

    }

}