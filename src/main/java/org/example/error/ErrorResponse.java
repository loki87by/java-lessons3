package org.example.error;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse extends Throwable {
    String error;
    String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

    @Override
    public String getMessage() {

        if (this.error.isEmpty() || (this.description != null && this.description.isEmpty())) {
            return "Error: " + this.error + this.description;
        } else {
            return this.error + this.description;
        }
    }
}
