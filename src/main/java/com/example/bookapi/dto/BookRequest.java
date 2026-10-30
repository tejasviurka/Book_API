package com.example.bookapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// What the client is ALLOWED to send. No id: the database owns the id.
public record BookRequest(

        @NotBlank(message = "title must not be blank")
        @Size(max = 200, message = "title must be at most 200 characters")
        String title,

        @NotBlank(message = "author must not be blank")
        @Size(max = 100, message = "author must be at most 100 characters")
        String author
) {
}