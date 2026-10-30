package com.example.bookapi.repository;

import com.example.bookapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// No method bodies needed: Spring Data creates the implementation at startup.
// You inherit save, findById, findAll, deleteById, existsById, count and more.
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}