package com.example.demoproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demoproject.model.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {

}
