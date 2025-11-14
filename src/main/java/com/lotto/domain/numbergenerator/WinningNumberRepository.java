package com.lotto.domain.numbergenerator;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WinningNumberRepository extends MongoRepository<WinningNumbers, String> {

    Optional<WinningNumbers> findByDate(LocalDateTime date);
    boolean existsByDate(LocalDateTime date);
}
