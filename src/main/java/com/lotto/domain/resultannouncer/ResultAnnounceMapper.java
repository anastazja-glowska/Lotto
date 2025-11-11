package com.lotto.domain.resultannouncer;

import com.lotto.domain.resultannouncer.dto.ResultDto;
import com.lotto.domain.resultchecker.dto.PlayerDto;

class ResultAnnounceMapper {

    static ResultDto mapFromResultAnswer(ResultAnswer resultAnswer) {
        return ResultDto.builder()
                .hash(resultAnswer.hash())
                .numbers(resultAnswer.numbers())
                .drawDate(resultAnswer.drawDate())
                .isWinner(resultAnswer.isWinner())
                .winningNumbers(resultAnswer.winningNumbers())
                .build();
    }

    static ResultDto mapFromPlayerDto(PlayerDto playerDto) {
        return ResultDto.builder()
                .winningNumbers(playerDto.winningNumbers())
                .isWinner(playerDto.isWinner())
                .drawDate(playerDto.drawDate())
                .numbers(playerDto.numbers())
                .hash(playerDto.hash())
                .build();
    }
}
