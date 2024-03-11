package com.vk.dto.posts;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CreatePostResponse {

    private Integer id;
    private String title;
    private String body;
    private Integer userId;
}
