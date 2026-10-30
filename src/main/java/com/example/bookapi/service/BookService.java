package com.example.bookapi.service;

import com.example.bookapi.dto.BookRequest;
import com.example.bookapi.dto.BookResponse;
import com.example.bookapi.exception.AuthorNotFoundException;
import com.example.bookapi.exception.BookNotFoundException;
import com.example.bookapi.model.Author;
import com.example.bookapi.model.Book;
import com.example.bookapi.repository.AuthorRepository;
import com.example.bookapi.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    // READ ALL (one JOIN query thanks to @EntityGraph in the repository)
    public List<BookResponse> findAll() {
        return bookRepository.findAll().stream()
                .map(BookResponse::from)
                .toList();
    }

    // READ ONE (throws BookNotFoundException -> 404)
    public BookResponse findById(Long id) {
        return BookResponse.from(getBookOrThrow(id));
    }

    // CREATE (throws AuthorNotFoundException -> 404 if authorId is unknown)
    public BookResponse create(BookRequest request) {
        Author author = getAuthorOrThrow(request.authorId());
        Book book = new Book(null, request.title(), author);
        return BookResponse.from(bookRepository.save(book));
    }

    // UPDATE
    public BookResponse update(Long id, BookRequest request) {
        Book existing = getBookOrThrow(id);
        existing.setTitle(request.title());
        existing.setAuthor(getAuthorOrThrow(request.authorId()));
        return BookResponse.from(bookRepository.save(existing));
    }

    // DELETE
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

    private Author getAuthorOrThrow(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }
}
