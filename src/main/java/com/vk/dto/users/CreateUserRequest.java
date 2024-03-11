package com.vk.dto.users;

import com.vk.dto.users.components.Address;
import com.vk.dto.users.components.Company;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CreateUserRequest {

    private String name;
    private String username;
    private String email;
    private Address address;
    private String phone;
    private String website;
    private Company company;

    @Override
    public String toString() {
        return "name=" + name +
                "; username=" + username +
                "; email=" + email +
                "; address=[" + address + "]" +
                "; phone=" + phone +
                "; website=" + website +
                "; company=[" + company + "]";
    }
}
