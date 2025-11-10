package com.lotto.domain.numberreceiver;

import org.jetbrains.annotations.NotNull;

class TicketNotFoundException extends RuntimeException{
    public TicketNotFoundException(String message) {
        super(message);
    }
}
