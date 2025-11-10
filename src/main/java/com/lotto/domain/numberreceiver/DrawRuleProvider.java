package com.lotto.domain.numberreceiver;

import java.time.LocalDate;
import java.time.LocalDateTime;

abstract class DrawRuleProvider {

    abstract boolean isDrawnToday(LocalDateTime date);
    abstract LocalDateTime getNextDrawnDate();
}
