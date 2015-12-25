package com.ajlopez.myapp2.service.impl;

import com.ajlopez.myapp2.service.CommentService;
import com.ajlopez.myapp2.domain.Comment;
import com.ajlopez.myapp2.repository.CommentRepository;
import com.ajlopez.myapp2.repository.search.CommentSearchRepository;
import com.ajlopez.myapp2.web.rest.dto.CommentDTO;
import com.ajlopez.myapp2.web.rest.mapper.CommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Comment.
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService{

    private final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);
    
    @Inject
    private CommentRepository commentRepository;
    
    @Inject
    private CommentMapper commentMapper;
    
    @Inject
    private CommentSearchRepository commentSearchRepository;
    
    /**
     * Save a comment.
     * @return the persisted entity
     */
    public CommentDTO save(CommentDTO commentDTO) {
        log.debug("Request to save Comment : {}", commentDTO);
        Comment comment = commentMapper.commentDTOToComment(commentDTO);
        comment = commentRepository.save(comment);
        CommentDTO result = commentMapper.commentToCommentDTO(comment);
        commentSearchRepository.save(comment);
        return result;
    }

    /**
     *  get all the comments.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Comment> findAll(Pageable pageable) {
        log.debug("Request to get all Comments");
        Page<Comment> result = commentRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one comment by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CommentDTO findOne(Long id) {
        log.debug("Request to get Comment : {}", id);
        Comment comment = commentRepository.findOne(id);
        CommentDTO commentDTO = commentMapper.commentToCommentDTO(comment);
        return commentDTO;
    }

    /**
     *  delete the  comment by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Comment : {}", id);
        commentRepository.delete(id);
        commentSearchRepository.delete(id);
    }

    /**
     * search for the comment corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<CommentDTO> search(String query) {
        
        log.debug("REST request to search Comments for query {}", query);
        return StreamSupport
            .stream(commentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(commentMapper::commentToCommentDTO)
            .collect(Collectors.toList());
    }
}
