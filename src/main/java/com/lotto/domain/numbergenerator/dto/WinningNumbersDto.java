package com.lotto.domain.numbergenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class WinningNumbersDto {


    public LocalDateTime date;
    public Set<Integer> winningNumbers;
}
