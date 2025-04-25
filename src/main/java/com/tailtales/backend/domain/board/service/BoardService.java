package com.tailtales.backend.domain.board.service;

import com.tailtales.backend.domain.board.dto.BoardListResponseDto;
import com.tailtales.backend.domain.common.dto.PageRequestDto;
import com.tailtales.backend.domain.common.dto.PageResponseDto;

public interface BoardService {

    PageResponseDto<BoardListResponseDto> getBoardList(PageRequestDto pageRequestDto);

    PageResponseDto<BoardListResponseDto> getBoardList(int categoryId, PageRequestDto pageRequestDto);

}
