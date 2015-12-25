package com.ajlopez.myapp2.service;

import com.ajlopez.myapp2.domain.Comment;
import com.ajlopez.myapp2.web.rest.dto.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Comment.
 */
public interface CommentService {

    /**
     * Save a comment.
     * @return the persisted entity
     */
    public CommentDTO save(CommentDTO commentDTO);

    /**
     *  get all the comments.
     *  @return the list of entities
     */
    public Page<Comment> findAll(Pageable pageable);

    /**
     *  get the "id" comment.
     *  @return the entity
     */
    public CommentDTO findOne(Long id);

    /**
     *  delete the "id" comment.
     */
    public void delete(Long id);

    /**
     * search for the comment corresponding
     * to the query.
     */
    public List<CommentDTO> search(String query);
}
