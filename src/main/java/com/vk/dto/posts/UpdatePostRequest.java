package com.vk.dto.posts;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UpdatePostRequest {

    private Integer id;
    private String title;
    private String body;
    private Integer userId;

    @Override
    public String toString() {
        return "id=" + id +
                "; title=" + title +
                "; body=" + body +
                "; userId=" + userId;
    }
}
