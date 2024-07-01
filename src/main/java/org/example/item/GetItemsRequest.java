package org.example.item;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetItemsRequest {
    Long userId;
    LINK_STATE state;
    LINK_CONTENT_TYPE contentType;
    List<String> tags = new ArrayList<>();
    LINK_SORT sort;
    int limit;

    public GetItemsRequest(Long userId, String state, String contentType, String sort, int limit, List<String> tags) {
        this.userId = userId;
        this.state = LINK_STATE.valueOf(state.toUpperCase());
        this.contentType = LINK_CONTENT_TYPE.valueOf(contentType.toUpperCase());
        this.tags = tags;
        this.sort = LINK_SORT.valueOf(sort.toUpperCase());
        this.limit = limit;
    }

    public GetItemsRequest(Long userId, String state, String contentType, String sort, int limit) {
        this.userId = userId;
        this.state = LINK_STATE.valueOf(state.toUpperCase());
        this.contentType = LINK_CONTENT_TYPE.valueOf(contentType.toUpperCase());
        this.sort = LINK_SORT.valueOf(sort.toUpperCase());
        this.limit = limit;
    }
}
