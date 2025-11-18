package com.lotto.infrastructure.resultannouncer.controller;


import com.lotto.domain.resultannouncer.ResultAnnouncerFacade;
import com.lotto.domain.resultannouncer.dto.ResultMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ResultAnnouncerRestController {
    
    private final ResultAnnouncerFacade resultAnnouncerFacade;

    @GetMapping("/results/{id}")
    public ResponseEntity<ResultMessageDto> checkResultsById(@PathVariable String id){
        ResultMessageDto result = resultAnnouncerFacade.checkPlayResultByHash(id);
        return  ResponseEntity.ok(result);
    }
}
