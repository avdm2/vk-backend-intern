package com.vk.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CacheLogDto {

    private String internalRequest;
    private String requestBody;

    @Override
    public int hashCode() {
        return 31 * (internalRequest.hashCode() + requestBody.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CacheLogDto log)) {
            return false;
        }

        return internalRequest.equals(log.internalRequest) &&
                requestBody.equals(log.requestBody);
    }
}
