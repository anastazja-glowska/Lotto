package com.lotto.domain.resultchecker;



public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String s) {
        super(s);
    }
}
