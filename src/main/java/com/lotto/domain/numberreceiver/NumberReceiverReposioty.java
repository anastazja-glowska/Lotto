package com.lotto.domain.numberreceiver;

import java.time.LocalDateTime;
import java.util.List;

public interface NumberReceiverReposioty {
    Ticket save(Ticket ticket);

    List<Ticket> findAllTicketsByDrawDate(LocalDateTime date);
}
