package com.vk.dto.posts;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class GetPostCommentResponse {

    private Integer postId;
    private Integer id;
    private String name;
    private String email;
    private String body;
}
