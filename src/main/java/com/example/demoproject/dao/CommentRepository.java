package com.example.demoproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demoproject.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
