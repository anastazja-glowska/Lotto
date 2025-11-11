package com.lotto.domain.resultchecker;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PlayersRepository extends MongoRepository<Player,String> {

    Optional<Player> findById(String ticketId);

}
