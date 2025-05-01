package com.tailtales.backend.domain.board.service;

import com.tailtales.backend.domain.board.dto.BoardResponseDto;
import com.tailtales.backend.domain.board.dto.BoardRequestDto;
import com.tailtales.backend.domain.board.dto.BoardUpdateRequestDto;
import com.tailtales.backend.domain.board.dto.BoardsResponseDto;
import com.tailtales.backend.domain.common.dto.PageRequestDto;
import com.tailtales.backend.domain.common.dto.PageResponseDto;

import java.util.Optional;

public interface BoardService {

    Long insertBoard(BoardRequestDto boardRequestDto);

    PageResponseDto<BoardsResponseDto> getBoardList(PageRequestDto pageRequestDto);

    PageResponseDto<BoardsResponseDto> getBoardList(int categoryId, PageRequestDto pageRequestDto);

    Optional<BoardResponseDto> getBoardInfo(long bno);

    Optional<BoardResponseDto> updateBoard(BoardUpdateRequestDto boardUpdateRequestDto);

    void deleteBoard(long bno);

}
