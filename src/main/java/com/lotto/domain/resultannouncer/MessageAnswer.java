package com.lotto.domain.resultannouncer;

enum MessageAnswer {

    ALREADY_CHECKED_INFO("Please come back later, you have already checked your result."),
    WIN_INFO("You are a winner! Congratulations!"),
    LOSE_INFO("Unfortunately, it was not your day. Try again!"),
    WAIT_INFO("Please come back later, the results are not known yet."),
    HASH_DOES_NOT_EXIST("A ticket with this hash does not exist.");

    final String message;


    MessageAnswer(String message) {
        this.message = message;
    }
}
