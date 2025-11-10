package com.lotto.domain.numberreceiver;

class HashGenerableTestImpl implements HashGenerable {

    private int counter = 0;

    @Override
    public String getHash() {
        counter++;
        return "abc" + counter;
    }
}
