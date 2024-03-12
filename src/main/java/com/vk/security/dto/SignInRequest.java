package com.vk.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class SignInRequest {

    private String username;
    private String password;
}
