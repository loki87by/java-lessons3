package org.example.error;

public class ItemRetrieverException extends RuntimeException {
    public ItemRetrieverException(String message) {
        super(message);
    }

    public ItemRetrieverException(String message, Throwable cause) {
        super(message, cause);
    }
}

