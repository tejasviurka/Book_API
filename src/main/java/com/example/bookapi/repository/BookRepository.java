package com.example.bookapi.repository;

import com.example.bookapi.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    // THE N+1 FIX. Without @EntityGraph, findAll() runs 1 query for books, then
    // 1 extra query per book when BookResponse.from() reads book.getAuthor().getName().
    // With it, Hibernate loads books AND authors in a single JOIN query.
    // Experiment: comment the annotation out and watch the SQL log.
    @Override
    @EntityGraph(attributePaths = "author")
    List<Book> findAll();

    @Override
    @EntityGraph(attributePaths = "author")
    Optional<Book> findById(Long id);

    // Derived query: where author_id = ?
    @EntityGraph(attributePaths = "author")
    List<Book> findByAuthorId(Long authorId);

    boolean existsByAuthorId(Long authorId);
}
