package com.tailtales.backend.domain.board.service.impl;

import com.tailtales.backend.domain.board.dto.BoardResponseDto;
import com.tailtales.backend.domain.board.dto.BoardsResponseDto;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

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
                        .name("관리자") // 관리자로 고정
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
                    .name("관리자") // 임시로 관리자로 고정
                    .content(board.getContent())
                    .viewCnt(board.getViewCnt())
                    .createdAt(board.getCreatedAt())
                    .build();
        });

    }

}