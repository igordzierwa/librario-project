package com.agh.librario.Book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByCategory(Category category);
    List<Book> findAll();
    Book findByName(String name);
    List<Book> findByStockCountGreaterThan(int number);
    List<Book> findByAuthor(String name);
}
