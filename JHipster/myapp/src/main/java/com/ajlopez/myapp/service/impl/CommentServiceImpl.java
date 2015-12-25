package com.ajlopez.myapp.service.impl;

import com.ajlopez.myapp.service.CommentService;
import com.ajlopez.myapp.domain.Comment;
import com.ajlopez.myapp.repository.CommentRepository;
import com.ajlopez.myapp.web.rest.dto.CommentDTO;
import com.ajlopez.myapp.web.rest.mapper.CommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Comment.
 */
@Service
public class CommentServiceImpl implements CommentService{

    private final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);
    
    @Inject
    private CommentRepository commentRepository;
    
    @Inject
    private CommentMapper commentMapper;
    
    /**
     * Save a comment.
     * @return the persisted entity
     */
    public CommentDTO save(CommentDTO commentDTO) {
        log.debug("Request to save Comment : {}", commentDTO);
        Comment comment = commentMapper.commentDTOToComment(commentDTO);
        comment = commentRepository.save(comment);
        CommentDTO result = commentMapper.commentToCommentDTO(comment);
        return result;
    }

    /**
     *  get all the comments.
     *  @return the list of entities
     */
    public Page<Comment> findAll(Pageable pageable) {
        log.debug("Request to get all Comments");
        Page<Comment> result = commentRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one comment by id.
     *  @return the entity
     */
    public CommentDTO findOne(String id) {
        log.debug("Request to get Comment : {}", id);
        Comment comment = commentRepository.findOne(id);
        CommentDTO commentDTO = commentMapper.commentToCommentDTO(comment);
        return commentDTO;
    }

    /**
     *  delete the  comment by id.
     */
    public void delete(String id) {
        log.debug("Request to delete Comment : {}", id);
        commentRepository.delete(id);
    }
}
