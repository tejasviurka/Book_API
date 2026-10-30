package com.example.bookapi.service;

import com.example.bookapi.dto.AuthorRequest;
import com.example.bookapi.dto.AuthorResponse;
import com.example.bookapi.dto.BookResponse;
import com.example.bookapi.exception.AuthorHasBooksException;
import com.example.bookapi.exception.AuthorNotFoundException;
import com.example.bookapi.model.Author;
import com.example.bookapi.repository.AuthorRepository;
import com.example.bookapi.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public AuthorResponse create(AuthorRequest request) {
        Author saved = authorRepository.save(new Author(null, request.name()));
        return AuthorResponse.from(saved);
    }

    public List<AuthorResponse> findAll() {
        return authorRepository.findAllWithBooks().stream()
                .map(AuthorResponse::from)
                .toList();
    }

    public AuthorResponse findById(Long id) {
        Author author = authorRepository.findWithBooksById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
        return AuthorResponse.from(author);
    }

    // All books of one author
    public List<BookResponse> findBooksByAuthor(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new AuthorNotFoundException(authorId);
        }
        return bookRepository.findByAuthorId(authorId).stream()
                .map(BookResponse::from)
                .toList();
    }

    // DELETE (404 if missing, 409 if the author still has books)
    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException(id);
        }
        if (bookRepository.existsByAuthorId(id)) {
            throw new AuthorHasBooksException(id);
        }
        authorRepository.deleteById(id);
    }
}
