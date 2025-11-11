package com.lotto.domain.resultannouncer;


import com.lotto.domain.resultannouncer.dto.ResultMessageDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResultAnnouncerFacade {

    private final ResultAnnounceRetriever resultAnnounceRetriever;

    public ResultMessageDto checkPlayResultByHash(String hash) {
        return resultAnnounceRetriever.checkPlayResultByHash(hash);
    }
}
