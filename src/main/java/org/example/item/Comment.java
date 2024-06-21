package org.example.item;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "owner_id", nullable = false, updatable = false)
    private Long ownerId;
    @Column(name = "item_id", nullable = false, updatable = false)
    private Long itemId;
    @Column(name = "description", nullable = false, length = 512)
    private String description;
    @Column(name = "feedback_date", insertable = false, updatable = false)
    private Timestamp feedbackDate;
}
