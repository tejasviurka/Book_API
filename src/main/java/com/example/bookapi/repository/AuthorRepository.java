package com.example.bookapi.repository;

import com.example.bookapi.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    // The other N+1: the AuthorResponse shows a book count, which touches author.getBooks().
    // JOIN FETCH loads authors with their books in one query. "distinct" removes duplicate
    // Author rows caused by the join.
    @Query("select distinct a from Author a left join fetch a.books")
    List<Author> findAllWithBooks();

    @Query("select a from Author a left join fetch a.books where a.id = :id")
    Optional<Author> findWithBooksById(@Param("id") Long id);
}
