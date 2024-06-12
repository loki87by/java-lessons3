package org.example.item;

import lombok.Data;

import java.util.Set;

@Data
public class ItemInfoWithUrlState implements ItemDTO {
    private String url;
    private String urlStatus;
    private Set<String> tags;

    public ItemInfoWithUrlState(Item item, String urlStatus) {
        this.tags = item.getTags();
        this.url = item.getUrl();
        this.urlStatus = urlStatus;
    }
}
