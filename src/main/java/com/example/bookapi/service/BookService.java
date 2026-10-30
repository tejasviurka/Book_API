package com.example.bookapi.service;

import com.example.bookapi.dto.BookRequest;
import com.example.bookapi.dto.BookResponse;
import com.example.bookapi.exception.BookNotFoundException;
import com.example.bookapi.model.Book;
import com.example.bookapi.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // READ ALL
    public List<BookResponse> findAll() {
        return bookRepository.findAll().stream()
                .map(BookResponse::from)
                .toList();
    }

    // READ ONE (throws BookNotFoundException -> 404)
    public BookResponse findById(Long id) {
        return BookResponse.from(getBookOrThrow(id));
    }

    // CREATE
    public BookResponse create(BookRequest request) {
        Book book = new Book(null, request.title(), request.author());
        return BookResponse.from(bookRepository.save(book));
    }

    // UPDATE (throws BookNotFoundException -> 404)
    public BookResponse update(Long id, BookRequest request) {
        Book existing = getBookOrThrow(id);
        existing.setTitle(request.title());
        existing.setAuthor(request.author());
        return BookResponse.from(bookRepository.save(existing));
    }

    // DELETE (throws BookNotFoundException -> 404)
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }

    private Book getBookOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }
}