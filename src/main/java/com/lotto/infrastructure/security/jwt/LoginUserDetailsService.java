package com.lotto.infrastructure.security.jwt;

import com.lotto.domain.loginandregister.LoginAndRegisterFacade;
import com.lotto.domain.loginandregister.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

@RequiredArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

    private final LoginAndRegisterFacade loginAndRegisterFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        UserDto retrievedUser = loginAndRegisterFacade.findByUserName(username);
        return getUser(retrievedUser);
    }

    private User getUser(UserDto userDto) {
        return new User(
                userDto.email(),
                userDto.password(),
                Collections.emptyList()
        );
    }
}
