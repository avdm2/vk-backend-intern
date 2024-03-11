package com.vk.dto.albums;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class GetAlbumResponse {

    private Integer userId;
    private Integer id;
    private String title;
}
