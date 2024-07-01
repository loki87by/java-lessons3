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
}
