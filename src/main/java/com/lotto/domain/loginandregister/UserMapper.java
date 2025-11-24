package com.lotto.domain.loginandregister;

import com.lotto.domain.loginandregister.dto.UserDto;
import com.lotto.domain.loginandregister.dto.UserRegisterResponseDto;

class UserMapper {


    static UserDto mapFromUser(User user) {
        return new UserDto(user.id(), user.email(), user.password());
    }


    static UserRegisterResponseDto mapFromUserToUserRegisterDto(User user) {

        return UserRegisterResponseDto.builder()
                .isRegistered(true)
                .id(user.id())
                .email(user.email())
                .build();
    }
}
