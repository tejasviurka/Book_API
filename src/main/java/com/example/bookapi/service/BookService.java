package com.example.bookapi.service;

import com.example.bookapi.dto.BookRequest;
import com.example.bookapi.dto.BookResponse;
import com.example.bookapi.dto.PageResponse;
import com.example.bookapi.exception.AuthorNotFoundException;
import com.example.bookapi.exception.BookNotFoundException;
import com.example.bookapi.exception.InvalidSortFieldException;
import com.example.bookapi.model.Author;
import com.example.bookapi.model.Book;
import com.example.bookapi.repository.AuthorRepository;
import com.example.bookapi.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    // Only these fields may be used in ?sort=... Anything else would reach the database
    // as an invalid column and become a 500, so we reject it early with a 400.
    private static final List<String> SORTABLE_FIELDS = List.of("id", "title", "author.name");

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    // READ ALL + search + paging + sorting in one method
    public PageResponse<BookResponse> search(String title, String author, Pageable pageable) {
        validateSort(pageable);
        Page<Book> page = bookRepository.search(clean(title), clean(author), pageable);
        return PageResponse.from(page.map(BookResponse::from));
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

    private void validateSort(Pageable pageable) {
        for (Sort.Order order : pageable.getSort()) {
            if (!SORTABLE_FIELDS.contains(order.getProperty())) {
                throw new InvalidSortFieldException(order.getProperty(), SORTABLE_FIELDS);
            }
        }
    }

    // null or blank filter -> "" (matches everything)
    private String clean(String value) {
        return value == null ? "" : value.trim();
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
