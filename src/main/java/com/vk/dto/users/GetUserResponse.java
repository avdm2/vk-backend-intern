package com.vk.dto.users;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.vk.dto.users.components.Address;
import com.vk.dto.users.components.Company;

@Getter
@Setter
@Accessors(chain = true)
public class GetUserResponse {

    private Integer id;
    private String name;
    private String username;
    private String email;
    private Address address;
    private String phone;
    private String website;
    private Company company;
}
