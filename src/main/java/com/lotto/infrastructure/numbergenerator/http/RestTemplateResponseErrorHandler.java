package com.lotto.infrastructure.numbergenerator.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.http.HttpStatus.Series;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;

class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {


    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus status = HttpStatus.valueOf(response.getRawStatusCode());
        Series series = status.series();

        if (series == HttpStatus.Series.SERVER_ERROR) {
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while using http client");
        } else if (series == Series.CLIENT_ERROR) {
            if(status == HttpStatus.NOT_FOUND){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            } else if (status == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }
    }
}
