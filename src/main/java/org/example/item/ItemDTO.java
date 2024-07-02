package org.example.item;

import lombok.Data;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
public class ItemDTO {
    private Long id;
    private String url;
    private Set<String> tags = new HashSet<>();
    private String resolvedUrl;
    private String mimeType;
    private String title;
    private boolean hasImage;
    private boolean hasVideo;
    private Timestamp dateResolved;
    boolean unread = true;

    public ItemDTO() {
    }

    public ItemDTO(Long id,
                   String url,
                   Set<String> tags,
                   String resolvedUrl,
                   String mimeType,
                   String title,
                   boolean hasImage,
                   boolean hasVideo,
                   Timestamp dateResolved,
                   boolean unread) {
        this.id = id;
        this.url = url;
        this.tags = tags;
        this.resolvedUrl = resolvedUrl;
        this.mimeType = mimeType;
        this.title = title;
        this.hasImage = hasImage;
        this.hasVideo = hasVideo;
        this.dateResolved = dateResolved;
        this.unread = unread;
    }
}
