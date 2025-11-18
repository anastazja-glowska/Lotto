package com.lotto.infrastructure.numberretriever.controller;

import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import com.lotto.domain.numberreceiver.dto.InputNumbersResponseDto;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
public class InputNumbersRestController {

    private final NumberReceiverFacade numberReceiverFacade;

    @PostMapping("/inputNumbers")
    public ResponseEntity<InputNumbersResponseDto> inputNumbers(@RequestBody @Valid InputNumbersRequestDto requestDto) {
        log.info("Received InputNumbersRequestDto {}", requestDto);

        Set<Integer> numbersFromUser = requestDto.inputNumbers().stream().collect(Collectors.toSet());
        InputNumbersResponseDto inputNumbers = numberReceiverFacade.inputNumbers(numbersFromUser);

        return ResponseEntity.ok(inputNumbers);
    }
}
