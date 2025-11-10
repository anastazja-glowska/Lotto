package com.lotto.domain.numbergenerator;

import org.jetbrains.annotations.NotNull;

class WinningNumbersNotFoundException extends RuntimeException{
    public WinningNumbersNotFoundException(String s) {
        super(s);
    }
}
