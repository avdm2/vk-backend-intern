package com.vk.dto.albums;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CreateAlbumRequest {

    private Integer userId;
    private String title;

    @Override
    public String toString() {
        return "userId=" + userId +
                "; title=" + title;
    }
}
