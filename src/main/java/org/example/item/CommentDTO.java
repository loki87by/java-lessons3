package org.example.item;

import java.sql.Timestamp;

public interface CommentDTO {
    Long getOwnerId();
    String getDescription();
    Timestamp getFeedbackDate();
}
