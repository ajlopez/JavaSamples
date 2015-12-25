package com.ajlopez.myapp.repository;

import com.ajlopez.myapp.domain.Comment;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Comment entity.
 */
public interface CommentRepository extends MongoRepository<Comment,String> {

}
