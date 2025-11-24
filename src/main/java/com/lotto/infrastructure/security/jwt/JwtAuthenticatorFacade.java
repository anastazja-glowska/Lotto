package com.lotto.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.lotto.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import com.lotto.infrastructure.loginandregister.controller.dto.TokenRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class JwtAuthenticatorFacade {

    private final AuthenticationManager authenticationManager;
    private final Clock clock;

    public JwtResponseDto authenticateAndGenerateToken(@Valid TokenRequestDto loginRequest) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        User user = (User) authenticate.getPrincipal();
        String token = createToken(user);
        String email = user.getUsername();

        return JwtResponseDto.builder()
                .token(token)
                .email(email)
                .build();
    }

    private String createToken(User user) {
        String secretKey = " abc";
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Instant now = LocalDateTime.now(clock).toInstant(ZoneOffset.UTC);
        Instant expirestAt = now.plus(Duration.ofDays(20));
        String issuer = "Lotto";
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer(issuer)
                .withIssuedAt(now)
                .withExpiresAt(expirestAt)
                .sign(algorithm);
    }
}
