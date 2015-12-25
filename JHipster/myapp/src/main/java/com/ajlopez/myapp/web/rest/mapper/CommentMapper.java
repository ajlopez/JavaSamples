package com.ajlopez.myapp.web.rest.mapper;

import com.ajlopez.myapp.domain.*;
import com.ajlopez.myapp.web.rest.dto.CommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Comment and its DTO CommentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CommentMapper {

    CommentDTO commentToCommentDTO(Comment comment);

    Comment commentDTOToComment(CommentDTO commentDTO);
}
