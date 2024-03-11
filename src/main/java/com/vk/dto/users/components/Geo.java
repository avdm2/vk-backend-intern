package com.vk.dto.users.components;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Geo {

    private String lat;
    private String lng;

    @Override
    public String toString() {
        return "lat=" + lat +
                "; lng=" + lng;
    }
}
