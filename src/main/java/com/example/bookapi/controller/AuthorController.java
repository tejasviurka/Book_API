package com.example.bookapi.controller;

import com.example.bookapi.dto.AuthorRequest;
import com.example.bookapi.dto.AuthorResponse;
import com.example.bookapi.dto.BookResponse;
import com.example.bookapi.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // GET /api/authors
    @GetMapping
    public List<AuthorResponse> getAll() {
        return authorService.findAll();
    }

    // GET /api/authors/1
    @GetMapping("/{id}")
    public AuthorResponse getById(@PathVariable Long id) {
        return authorService.findById(id);
    }

    // GET /api/authors/1/books
    @GetMapping("/{id}/books")
    public List<BookResponse> getBooks(@PathVariable Long id) {
        return authorService.findBooksByAuthor(id);
    }

    // POST /api/authors
    @PostMapping
    public ResponseEntity<AuthorResponse> create(@Valid @RequestBody AuthorRequest request) {
        AuthorResponse created = authorService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    // DELETE /api/authors/1
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }
}
