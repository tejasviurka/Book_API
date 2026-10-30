package com.example.bookapi.dto;

import com.example.bookapi.model.Book;

// What the API sends BACK. The entity never leaves the service layer.
public record BookResponse(Long id, String title, String author) {

    public static BookResponse from(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor());
    }
}