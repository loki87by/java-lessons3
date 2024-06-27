package org.example.item;

import jakarta.persistence.*;

import lombok.Data;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "url", nullable = false)
    private String url;
    @ElementCollection
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name="name")
    private Set<String> tags = new HashSet<>();
    @Column(name = "resolved_url", nullable = false, length = 512)
    private String resolvedUrl;
    @Column(name = "mime_type", nullable = false)
    private String mimeType;
    @Column(name = "title", nullable = false, length = 512)
    private String title;
    @Column(name = "has_image")
    private boolean hasImage;
    @Column(name = "has_video")
    private boolean hasVideo;
    @Column(name = "date_resolved", nullable = false)
    private Instant dateResolved;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id) &&
                Objects.equals(userId, item.userId) &&
                Objects.equals(url, item.url) &&
                Objects.equals(tags, item.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, url, tags);
    }
}
