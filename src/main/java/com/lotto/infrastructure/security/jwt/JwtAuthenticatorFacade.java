package com.lotto.infrastructure.security.jwt;

import com.lotto.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import com.lotto.infrastructure.loginandregister.controller.dto.TokenRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticatorFacade {

    private final AuthenticationManager authenticationManager;

    public JwtResponseDto authenticateAndGenerateToken(@Valid TokenRequestDto loginRequest) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );
        return JwtResponseDto.builder().build();
    }
}
