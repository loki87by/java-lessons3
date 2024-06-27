package org.example.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class UrlMetadataExceptionHandler {

    @ExceptionHandler(value = {ItemRetrieverException.class})
    public ResponseEntity<String> handleItemRetrieverException(ItemRetrieverException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching URL metadata: " + ex.getMessage());
    }
}

