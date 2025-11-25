package com.lotto.infrastructure.loginandregister.controller.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Log4j2
public class TokenControllerErrorHandler {


    private static final String BAD_CREDENTIALS = "Bad Credentials";
    private static final String DUPLICATE_KEY = "Duplicate key exception was thrown";
    private static final String USER_ALREADY_EXISTS = "User with this email already exists";

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public RegisterErrorResponseDto handleDuplicateKeyException(DuplicateKeyException exception){
        log.error(DUPLICATE_KEY + exception.getMessage());
        return new RegisterErrorResponseDto(USER_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public RegisterErrorResponseDto handleBadCredentialsException(BadCredentialsException exception){
        log.error(BAD_CREDENTIALS + exception.getMessage());
        return new RegisterErrorResponseDto(BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED);
    }
}
