package com.example.bookapi.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

// One consistent error shape for the whole API.
// fieldErrors is only included when there are validation problems.
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL) Map<String, String> fieldErrors
) {
}