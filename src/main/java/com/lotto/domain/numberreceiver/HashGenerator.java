package com.lotto.domain.numberreceiver;


import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Supplier;


class HashGenerator implements HashGenerable{


    private final Supplier<String> generateHash = () -> UUID.randomUUID().toString();

    @Override
    public String getHash() {
        return generateHash.get();
    }


}
