package com.vk.dto.users.components;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Company {

    private String name;
    private String catchPhrase;
    private String bs;

    @Override
    public String toString() {
        return "name=" + name +
                "; catchPhrase=" + catchPhrase +
                "; bs=" + bs;
    }
}
