package com.vk.dto.albums;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class GetAlbumCommentsResponse {

    private Integer postId;
    private Integer id;
    private String name;
    private String email;
    private String body;
}
