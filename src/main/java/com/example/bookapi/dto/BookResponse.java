package com.example.bookapi.dto;

import com.example.bookapi.model.Book;

// Flat shape: no nested Author entity, so no recursion and no lazy-loading surprises.
public record BookResponse(Long id, String title, Long authorId, String authorName) {

    public static BookResponse from(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                book.getAuthor().getName());
    }
}