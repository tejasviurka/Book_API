package com.example.bookapi.exception;

// Business rule: an author who still has books cannot be deleted -> 409 Conflict.
public class AuthorHasBooksException extends RuntimeException {

    public AuthorHasBooksException(Long id) {
        super("Author " + id + " still has books. Delete or move the books first.");
    }
}
