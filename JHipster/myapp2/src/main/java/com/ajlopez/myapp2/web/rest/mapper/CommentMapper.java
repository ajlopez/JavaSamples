package com.ajlopez.myapp2.web.rest.mapper;

import com.ajlopez.myapp2.domain.*;
import com.ajlopez.myapp2.web.rest.dto.CommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Comment and its DTO CommentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CommentMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.lastName", target = "authorLastName")
    CommentDTO commentToCommentDTO(Comment comment);

    @Mapping(source = "authorId", target = "author")
    Comment commentDTOToComment(CommentDTO commentDTO);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
