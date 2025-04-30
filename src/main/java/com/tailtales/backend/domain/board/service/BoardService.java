package com.tailtales.backend.domain.board.service;

import com.tailtales.backend.domain.board.dto.BoardResponseDto;
import com.tailtales.backend.domain.board.dto.PostRequestDto;
import com.tailtales.backend.domain.board.dto.BoardsResponseDto;
import com.tailtales.backend.domain.common.dto.PageRequestDto;
import com.tailtales.backend.domain.common.dto.PageResponseDto;

import java.util.Optional;

public interface BoardService {

    PageResponseDto<BoardsResponseDto> getBoardList(PageRequestDto pageRequestDto);

    PageResponseDto<BoardsResponseDto> getBoardList(int categoryId, PageRequestDto pageRequestDto);

    Optional<BoardResponseDto> getBoardInfo(long bno);

    Long insertBoard(PostRequestDto postRequestDto);

}
