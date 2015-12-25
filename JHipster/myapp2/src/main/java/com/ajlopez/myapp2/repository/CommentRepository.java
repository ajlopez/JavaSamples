package com.ajlopez.myapp2.repository;

import com.ajlopez.myapp2.domain.Comment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Comment entity.
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {

}
