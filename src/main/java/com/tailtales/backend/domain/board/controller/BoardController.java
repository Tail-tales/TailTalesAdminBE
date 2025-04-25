package com.tailtales.backend.domain.board.controller;

import com.tailtales.backend.domain.board.dto.BoardListResponseDto;
import com.tailtales.backend.domain.board.service.BoardService;
import com.tailtales.backend.domain.common.dto.PageRequestDto;
import com.tailtales.backend.domain.common.dto.PageResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/boards")
@Tag(name = "Board", description = "Board API")
public class BoardController {

    private final BoardService boardService;

    // 전체 글 조회
    @GetMapping("/all")
    public ResponseEntity<PageResponseDto<BoardListResponseDto>> getBoardList(
            @ModelAttribute PageRequestDto pageRequestDto) {

        PageResponseDto<BoardListResponseDto> response = boardService.getBoardList(pageRequestDto);
        return ResponseEntity.ok().body(response);

    }

    // 카테고리별 글 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageResponseDto<BoardListResponseDto>> getBoardList(
            @PathVariable(name = "categoryId") int categoryId,
            @ModelAttribute PageRequestDto pageRequestDto) {

        PageResponseDto<BoardListResponseDto> response = boardService.getBoardList(categoryId, pageRequestDto);
        return ResponseEntity.ok().body(response);

    }

}
