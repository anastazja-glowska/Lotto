package com.lotto.domain.numbergenerator.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class WinningNumbersDto {


    private LocalDateTime date;
    private Set<Integer> winningNumbers;
}
