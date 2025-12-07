package com.lotto.domain.resultannouncer;

import com.lotto.domain.resultannouncer.dto.ResultDto;
import com.lotto.domain.resultannouncer.dto.ResultMessageDto;
import com.lotto.domain.resultchecker.ResultCheckerFacade;
import com.lotto.domain.resultchecker.dto.PlayerDto;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
class ResultAnnounceRetriever {

    private final ResultRepository resultRepository;
    private final ResultCheckerFacade resultChecker;
    private final Clock clock;

    ResultMessageDto checkPlayResultByHash(String hash) {
        if (resultRepository.existsById(hash)) {
            Optional<ResultAnswer> cachedResult = resultRepository.findById(hash);
            if (cachedResult.isPresent()) {
                ResultAnswer resultAnswer = cachedResult.get();
                ResultDto resultDto = ResultAnnounceMapper.mapFromResultAnswer(resultAnswer);
                if (!hasDrawDatePassed(resultDto.drawDate())) {
                    return new ResultMessageDto(MessageAnswer.WAIT_INFO.message, resultDto);
                }

                return new ResultMessageDto(MessageAnswer.ALREADY_CHECKED_INFO.message, resultDto);
            }

        }


        PlayerDto playerDto = resultChecker.findPlayerByTicketId(hash);
        if (playerDto == null) {
            return new ResultMessageDto(MessageAnswer.HASH_DOES_NOT_EXIST.message, ResultDto.builder().build());
        }

        ResultDto resultDto = ResultAnnounceMapper.mapFromPlayerDto(playerDto);
        ResultAnswer resultAnswer = toResultAnswer(resultDto, LocalDateTime.now(clock));
        resultRepository.save(resultAnswer);


        if (resultChecker.findPlayerByTicketId(hash).isWinner()) {
            return new ResultMessageDto(MessageAnswer.WIN_INFO.message, resultDto);
        }

        return new ResultMessageDto(MessageAnswer.LOSE_INFO.message, resultDto);


    }

    private boolean hasDrawDatePassed(LocalDateTime drawDate) {
        return LocalDateTime.now(clock).isAfter(drawDate);
    }

    private static ResultAnswer toResultAnswer(ResultDto resultDto, LocalDateTime now) {
        return ResultAnswer.builder()
                .numbers(resultDto.numbers())
                .winningNumbers(resultDto.winningNumbers())
                .isWinner(resultDto.isWinner())
                .hash(resultDto.hash())
                .drawDate(resultDto.drawDate())
                .timeOfCreation(now)
                .build();
    }
}
