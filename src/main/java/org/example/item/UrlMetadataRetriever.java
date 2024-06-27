package org.example.item;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public interface UrlMetadataRetriever {
    interface UrlMetadata{
        String getNormalUrl();
        String getResolvedUrl();
        String getMimeType();
        String getTitle();
        boolean isHasImage();
        boolean isHasVideo();
        Timestamp getDateResolved();
    }
    UrlMetadata retrieve (String url);
}
