package com.lotto.infrastructure.loginandregister.controller;

import com.lotto.domain.loginandregister.LoginAndRegisterFacade;
import com.lotto.domain.loginandregister.dto.UserRegisterRequestDto;
import com.lotto.domain.loginandregister.dto.UserRegisterResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
class RegisterController {

    private final LoginAndRegisterFacade loginAndRegisterFacade;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> register(@RequestBody @Valid UserRegisterRequestDto registerUserDto) {

        String encodedPassword = passwordEncoder.encode(registerUserDto.password());
        UserRegisterResponseDto registered = loginAndRegisterFacade.register(
                new UserRegisterRequestDto(registerUserDto.email(), encodedPassword)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(registered);

    }

}
