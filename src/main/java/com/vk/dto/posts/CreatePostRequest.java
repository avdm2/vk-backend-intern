package com.vk.dto.posts;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CreatePostRequest {

    private String title;
    private String body;
    private Integer userId;

    @Override
    public String toString() {
        return "title=" + title +
                "; body=" + body +
                "; userId=" + userId;
    }
}
