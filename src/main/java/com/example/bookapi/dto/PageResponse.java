package com.example.bookapi.dto;

import org.springframework.data.domain.Page;

import java.util.List;

// Our own stable page shape. We do NOT return Spring's Page object directly,
// because its JSON format is an internal detail that can change between versions.
public record PageResponse<T>(
        List<T> content,
        int page,            // current page number, starts at 0
        int size,            // page size requested
        long totalElements,  // total rows matching the search
        int totalPages,
        boolean last) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }
}
