package com.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.function.Predicate;

@AllArgsConstructor
class DrawDateGenerator extends DrawRuleProvider{


    private static final LocalTime DRAW_TIME = LocalTime.of(12,0, 0);
    private static final TemporalAdjuster NEXT_DRAWN_DATE = TemporalAdjusters.next(DayOfWeek.SATURDAY);
    private final Clock clock;

    @Override
    boolean isDrawnToday(LocalDateTime date) {
        Predicate<LocalDateTime> isDrawn = dateTime ->
                dateTime.getDayOfWeek() == DayOfWeek.SATURDAY && dateTime.toLocalTime().isBefore(DRAW_TIME);

        return isDrawn.test(date);
    }

    @Override
    LocalDateTime getNextDrawnDate() {

        LocalDateTime now = LocalDateTime.now(clock);
        if(isDrawnToday(now)){
            return LocalDateTime.of(now.toLocalDate(), DRAW_TIME);
        }

        LocalDateTime nextDrawnDate = now.with(NEXT_DRAWN_DATE);
        return LocalDateTime.of(nextDrawnDate.toLocalDate(), DRAW_TIME);
    }
}
