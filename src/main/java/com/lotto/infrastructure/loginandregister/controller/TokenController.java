package com.lotto.infrastructure.loginandregister.controller;

import com.lotto.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import com.lotto.infrastructure.loginandregister.controller.dto.TokenRequestDto;
import com.lotto.infrastructure.security.jwt.JwtAuthenticatorFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class TokenController {

    private final JwtAuthenticatorFacade jwtAuthenticatorFacade;

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> authenticateAndGenerateToken(@RequestBody @Valid TokenRequestDto loginRequest) {
        JwtResponseDto jwtResponseDto = jwtAuthenticatorFacade.authenticateAndGenerateToken(loginRequest);
        return ResponseEntity.ok(jwtResponseDto);
    }
}
