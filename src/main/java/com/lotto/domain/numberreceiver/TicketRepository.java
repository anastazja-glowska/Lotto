package com.lotto.domain.numberreceiver;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface TicketRepository extends MongoRepository<Ticket,String> {

    Collection<Ticket> findAllTicketsByDrawDate(LocalDateTime date);

    Optional<Ticket> findTicketByHash(String hash);
}
