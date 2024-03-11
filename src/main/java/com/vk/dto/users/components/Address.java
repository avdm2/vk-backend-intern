package com.vk.dto.users.components;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Address {

    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private Geo geo;

    @Override
    public String toString() {
        return "street=" + street +
                "; suite=" + suite +
                "; city=" + city +
                "; zipcode=" + zipcode +
                "; geo=[" + geo + "]";
    }
}
