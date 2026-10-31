package com.example.bookapi.exception;

import java.util.List;

// Thrown when the client asks to sort by a field we do not allow -> 400.
public class InvalidSortFieldException extends RuntimeException {

    public InvalidSortFieldException(String field, List<String> allowed) {
        super("Cannot sort by '" + field + "'. Allowed fields: " + allowed);
    }
}
