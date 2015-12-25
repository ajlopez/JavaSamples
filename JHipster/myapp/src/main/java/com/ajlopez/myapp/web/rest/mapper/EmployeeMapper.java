package com.ajlopez.myapp.web.rest.mapper;

import com.ajlopez.myapp.domain.*;
import com.ajlopez.myapp.web.rest.dto.EmployeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Employee and its DTO EmployeeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmployeeMapper {

    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    Employee employeeDTOToEmployee(EmployeeDTO employeeDTO);
}
