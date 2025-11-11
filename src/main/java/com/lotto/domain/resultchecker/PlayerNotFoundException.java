package com.lotto.domain.resultchecker;

import org.jetbrains.annotations.NotNull;

class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String s) {
        super(s);
    }
}
