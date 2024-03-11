package com.vk.dto.albums;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateAlbumRequest {

    private Integer userId;
    private Integer id;
    private String title;

    @Override
    public String toString() {
        return "id=" + id +
                "; userId=" + userId +
                "; title=" + title;
    }
}
