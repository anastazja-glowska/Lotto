package com.lotto.domain.loginandregister;

import com.lotto.domain.loginandregister.dto.UserRegisterResponseDto;

class UserMapper {


    static UserRegisterResponseDto mapFromUserToUserRegisterDto(User user) {

        return UserRegisterResponseDto.builder()
                .isRegistered(true)
                .id(user.id())
                .email(user.email())
                .build();
    }
}
