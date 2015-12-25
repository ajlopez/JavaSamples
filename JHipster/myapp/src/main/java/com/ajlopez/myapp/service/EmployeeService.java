package com.ajlopez.myapp.service;

import com.ajlopez.myapp.domain.Employee;
import com.ajlopez.myapp.web.rest.dto.EmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Employee.
 */
public interface EmployeeService {

    /**
     * Save a employee.
     * @return the persisted entity
     */
    public EmployeeDTO save(EmployeeDTO employeeDTO);

    /**
     *  get all the employees.
     *  @return the list of entities
     */
    public Page<Employee> findAll(Pageable pageable);

    /**
     *  get the "id" employee.
     *  @return the entity
     */
    public EmployeeDTO findOne(String id);

    /**
     *  delete the "id" employee.
     */
    public void delete(String id);
}
