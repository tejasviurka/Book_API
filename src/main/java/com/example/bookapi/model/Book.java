package com.example.bookapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity                      // this class maps to a database table
@Table(name = "books")       // table name
@Data
@NoArgsConstructor           // JPA and Jackson both need a no-args constructor
@AllArgsConstructor
public class Book {

    @Id                                                   // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // database auto-generates the id
    private Long id;

    private String title;
    private String author;
}