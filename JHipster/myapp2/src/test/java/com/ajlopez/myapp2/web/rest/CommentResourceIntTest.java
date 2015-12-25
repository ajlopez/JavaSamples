package com.ajlopez.myapp2.web.rest;

import com.ajlopez.myapp2.Application;
import com.ajlopez.myapp2.domain.Comment;
import com.ajlopez.myapp2.repository.CommentRepository;
import com.ajlopez.myapp2.service.CommentService;
import com.ajlopez.myapp2.web.rest.dto.CommentDTO;
import com.ajlopez.myapp2.web.rest.mapper.CommentMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CommentResource REST controller.
 *
 * @see CommentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CommentResourceIntTest {

    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private CommentMapper commentMapper;

    @Inject
    private CommentService commentService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCommentMockMvc;

    private Comment comment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommentResource commentResource = new CommentResource();
        ReflectionTestUtils.setField(commentResource, "commentService", commentService);
        ReflectionTestUtils.setField(commentResource, "commentMapper", commentMapper);
        this.restCommentMockMvc = MockMvcBuilders.standaloneSetup(commentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        comment = new Comment();
        comment.setContent(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createComment() throws Exception {
        int databaseSizeBeforeCreate = commentRepository.findAll().size();

        // Create the Comment
        CommentDTO commentDTO = commentMapper.commentToCommentDTO(comment);

        restCommentMockMvc.perform(post("/api/comments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commentDTO)))
                .andExpect(status().isCreated());

        // Validate the Comment in the database
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(databaseSizeBeforeCreate + 1);
        Comment testComment = comments.get(comments.size() - 1);
        assertThat(testComment.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = commentRepository.findAll().size();
        // set the field null
        comment.setContent(null);

        // Create the Comment, which fails.
        CommentDTO commentDTO = commentMapper.commentToCommentDTO(comment);

        restCommentMockMvc.perform(post("/api/comments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commentDTO)))
                .andExpect(status().isBadRequest());

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllComments() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get all the comments
        restCommentMockMvc.perform(get("/api/comments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(comment.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

        // Get the comment
        restCommentMockMvc.perform(get("/api/comments/{id}", comment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(comment.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingComment() throws Exception {
        // Get the comment
        restCommentMockMvc.perform(get("/api/comments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

		int databaseSizeBeforeUpdate = commentRepository.findAll().size();

        // Update the comment
        comment.setContent(UPDATED_CONTENT);
        CommentDTO commentDTO = commentMapper.commentToCommentDTO(comment);

        restCommentMockMvc.perform(put("/api/comments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commentDTO)))
                .andExpect(status().isOk());

        // Validate the Comment in the database
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(databaseSizeBeforeUpdate);
        Comment testComment = comments.get(comments.size() - 1);
        assertThat(testComment.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void deleteComment() throws Exception {
        // Initialize the database
        commentRepository.saveAndFlush(comment);

		int databaseSizeBeforeDelete = commentRepository.findAll().size();

        // Get the comment
        restCommentMockMvc.perform(delete("/api/comments/{id}", comment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
