package com.vk.security.dto;

import com.vk.security.entities.components.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class SignUpRequest {

    private String username;
    private String password;
    private Role role;
}
