package com.tailtales.backend.domain.board.service;

import com.tailtales.backend.domain.board.dto.BoardResponseDto;
import com.tailtales.backend.domain.board.dto.BoardRequestDto;
import com.tailtales.backend.domain.board.dto.BoardUpdateRequestDto;
import com.tailtales.backend.domain.board.dto.BoardsResponseDto;
import com.tailtales.backend.domain.common.dto.PageRequestDto;
import com.tailtales.backend.domain.common.dto.PageResponseDto;

import java.util.List;
import java.util.Optional;

public interface BoardService {

    Long insertBoard(BoardRequestDto boardRequestDto, String adminAccessToken);

    PageResponseDto<BoardsResponseDto> getBoardList(String sort, PageRequestDto pageRequestDto, String adminAccessToken);

    PageResponseDto<BoardsResponseDto> getBoardList(String sort, List<Integer> categoryIds, PageRequestDto pageRequestDto, String adminAccessToken);

    Optional<BoardResponseDto> getBoardInfo(long bno, String adminAccessToken);

    Optional<BoardResponseDto> updateBoard(BoardUpdateRequestDto boardUpdateRequestDto, String adminAccessToken);

    void deleteBoard(long bno, String adminAccessToken);

}
