package com.example.demoproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demoproject.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
