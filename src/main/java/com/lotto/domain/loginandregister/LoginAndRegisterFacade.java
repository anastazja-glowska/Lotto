package com.lotto.domain.loginandregister;

import com.lotto.domain.loginandregister.dto.UserDto;
import com.lotto.domain.loginandregister.dto.UserRegisterRequestDto;
import com.lotto.domain.loginandregister.dto.UserRegisterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LoginAndRegisterFacade {

    private final UserAdder userAdder;
    private final UserRetriever userRetriever;

    public UserDto findByUserName(String userName){
        return userRetriever.findByUserName(userName);
    }

    public UserRegisterResponseDto register(UserRegisterRequestDto user) {
        return userAdder.register(user);
    }
}
