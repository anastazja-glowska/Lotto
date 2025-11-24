package com.lotto.domain.resultannouncer;


import com.lotto.domain.resultannouncer.dto.ResultMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@RequiredArgsConstructor
public class ResultAnnouncerFacade {

    private final ResultAnnounceRetriever resultAnnounceRetriever;

    @Cacheable("lotto")
    public ResultMessageDto checkPlayResultByHash(String hash) {
        return resultAnnounceRetriever.checkPlayResultByHash(hash);
    }
}
