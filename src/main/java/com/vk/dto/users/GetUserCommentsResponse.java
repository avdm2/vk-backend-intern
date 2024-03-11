package com.vk.dto.users;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class GetUserCommentsResponse {

    private Integer postId;
    private Integer id;
    private String name;
    private String email;
    private String body;
}
