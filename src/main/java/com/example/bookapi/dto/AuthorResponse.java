package com.example.bookapi.dto;

import com.example.bookapi.model.Author;

// Note: we return a bookCount, NOT the list of Book entities.
// Returning entities with a two-way relationship would loop forever in JSON.
public record AuthorResponse(Long id, String name, int bookCount) {

    public static AuthorResponse from(Author author) {
        return new AuthorResponse(author.getId(), author.getName(), author.getBooks().size());
    }
}
