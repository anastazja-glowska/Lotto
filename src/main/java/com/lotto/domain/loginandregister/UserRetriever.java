package com.lotto.domain.loginandregister;

import com.lotto.domain.loginandregister.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class UserRetriever {

    private static final String USER_NOT_FOUND = "User not found";

    private final UserRepository userRepository;

    UserDto findByUserName(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException(USER_NOT_FOUND));
        return UserMapper.mapFromUser(user);

    }
}
