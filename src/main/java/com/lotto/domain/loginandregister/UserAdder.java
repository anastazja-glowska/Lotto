package com.lotto.domain.loginandregister;

import com.lotto.domain.loginandregister.dto.UserRegisterRequestDto;
import com.lotto.domain.loginandregister.dto.UserRegisterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class UserAdder {


    private final UserRepository userRepository;
    UserRegisterResponseDto register(UserRegisterRequestDto user) {


        User userToSave = User.builder()
                .email(user.email())
                .password(user.password())
                .build();

        User saved = userRepository.save(userToSave);

        return UserMapper.mapFromUserToUserRegisterDto(saved);

    }
}
