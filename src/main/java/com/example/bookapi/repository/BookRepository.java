package com.example.bookapi.repository;

import com.example.bookapi.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(attributePaths = "author")
    List<Book> findAll();

    @Override
    @EntityGraph(attributePaths = "author")
    Optional<Book> findById(Long id);

    @EntityGraph(attributePaths = "author")
    List<Book> findByAuthorId(Long authorId);

    boolean existsByAuthorId(Long authorId);

    // --- Stage 6 ---

    // DERIVED QUERY: Spring builds the query from the method name.
    // Containing = LIKE %title%, IgnoreCase = case-insensitive, Pageable = page + sort.
    @EntityGraph(attributePaths = "author")
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // @Query: used when the name would get too long or we need two filters together.
    // An empty string matches everything, so the service passes "" for a missing filter.
    // countQuery is the extra "select count" Spring runs to fill totalElements/totalPages.
    @EntityGraph(attributePaths = "author")
    @Query(value = "select b from Book b join b.author a "
            + "where lower(b.title) like lower(concat('%', :title, '%')) "
            + "and lower(a.name) like lower(concat('%', :author, '%'))",
            countQuery = "select count(b) from Book b join b.author a "
                    + "where lower(b.title) like lower(concat('%', :title, '%')) "
                    + "and lower(a.name) like lower(concat('%', :author, '%'))")
    Page<Book> search(@Param("title") String title,
                      @Param("author") String author,
                      Pageable pageable);
}
