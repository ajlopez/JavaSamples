package com.ajlopez.myapp2.web.rest.mapper;

import com.ajlopez.myapp2.domain.*;
import com.ajlopez.myapp2.web.rest.dto.EmployeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Employee and its DTO EmployeeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmployeeMapper {

    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    Employee employeeDTOToEmployee(EmployeeDTO employeeDTO);
}
