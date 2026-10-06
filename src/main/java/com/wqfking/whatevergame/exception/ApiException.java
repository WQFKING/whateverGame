package com.wqfking.whatevergame.exception;

import java.util.Objects;

import org.springframework.http.HttpStatusCode;

/** An expected API failure with a stable client-facing error code. */
public class ApiException extends RuntimeException {

    private final HttpStatusCode status;
    private final String code;

    public ApiException(HttpStatusCode status, String code, String message) {
        super(Objects.requireNonNull(message, "message"));
        this.status = Objects.requireNonNull(status, "status");
        this.code = Objects.requireNonNull(code, "code");
        if (!status.isError()) {
            throw new IllegalArgumentException("API exceptions require a 4xx or 5xx status");
        }
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
