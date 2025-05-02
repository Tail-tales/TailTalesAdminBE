package com.tailtales.backend.domain.board.controller;

import com.tailtales.backend.domain.board.dto.BoardResponseDto;
import com.tailtales.backend.domain.board.dto.BoardUpdateRequestDto;
import com.tailtales.backend.domain.board.dto.BoardsResponseDto;
import com.tailtales.backend.domain.board.dto.BoardRequestDto;
import com.tailtales.backend.domain.board.service.BoardService;
import com.tailtales.backend.domain.common.dto.PageRequestDto;
import com.tailtales.backend.domain.common.dto.PageResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/boards")
@Tag(name = "Board", description = "Board API")
public class BoardController {

    private final BoardService boardService;

    // 전체 글 조회
    @GetMapping("/all")
    public ResponseEntity<PageResponseDto<BoardsResponseDto>> getBoardList(
            @ModelAttribute PageRequestDto pageRequestDto) {

        PageResponseDto<BoardsResponseDto> response = boardService.getBoardList(pageRequestDto);
        return ResponseEntity.ok().body(response);

    }

    // 카테고리별 글 조회
    @GetMapping("/category")
    public ResponseEntity<PageResponseDto<BoardsResponseDto>> getBoardsByCategory(
            @RequestParam(name = "categoryIds") List<Integer> categoryIds,
            @ModelAttribute PageRequestDto pageRequestDto) {

        PageResponseDto<BoardsResponseDto> response = boardService.getBoardList(categoryIds, pageRequestDto);
        return ResponseEntity.ok().body(response);

    }

    // 개별 글 조회
    @GetMapping("/{bno}")
    public ResponseEntity<BoardResponseDto> getBoardInfo(@PathVariable("bno") long bno) {

        Optional<BoardResponseDto> response = boardService.getBoardInfo(bno);

        return response.map(boardResponseDto -> new ResponseEntity<>(boardResponseDto, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    // 글 작성
    @PostMapping
    public ResponseEntity<Long> insertBoard(@Valid @RequestBody BoardRequestDto boardRequestDto) {

        Long boardId = boardService.insertBoard(boardRequestDto);
        return new ResponseEntity<>(boardId, HttpStatus.CREATED);

    }

    // 글 수정
    @PutMapping("/edit")
    public ResponseEntity<?> updateBoard(@Valid @RequestBody BoardUpdateRequestDto boardUpdateRequestDto) {
        Optional<BoardResponseDto> updatedBoardOptional = boardService.updateBoard(boardUpdateRequestDto);

        if (updatedBoardOptional.isPresent()) {
            return new ResponseEntity<>(updatedBoardOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("게시글 수정에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR); // 또는 다른 적절한 상태 코드
        }
    }

    // 글 삭제
    @DeleteMapping("/{bno}")
    public ResponseEntity<?> deleteBoard(@PathVariable("bno") long bno) {

        boardService.deleteBoard(bno);
        return new ResponseEntity<>("게시글이 성공적으로 삭제되었습니다.", HttpStatus.OK);

    }

}
