package com.lotto.domain.numbergenerator;

public class NumberOutOfRangeException extends RuntimeException {
    public NumberOutOfRangeException(String s) {
        super(s);
    }
}
