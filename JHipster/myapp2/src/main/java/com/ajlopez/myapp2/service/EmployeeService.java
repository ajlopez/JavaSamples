package com.ajlopez.myapp2.service;

import com.ajlopez.myapp2.domain.Employee;
import com.ajlopez.myapp2.web.rest.dto.EmployeeDTO;
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
    public EmployeeDTO findOne(Long id);

    /**
     *  delete the "id" employee.
     */
    public void delete(Long id);

    /**
     * search for the employee corresponding
     * to the query.
     */
    public List<EmployeeDTO> search(String query);
}
