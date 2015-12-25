package com.ajlopez.myapp.service.impl;

import com.ajlopez.myapp.service.EmployeeService;
import com.ajlopez.myapp.domain.Employee;
import com.ajlopez.myapp.repository.EmployeeRepository;
import com.ajlopez.myapp.web.rest.dto.EmployeeDTO;
import com.ajlopez.myapp.web.rest.mapper.EmployeeMapper;
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
 * Service Implementation for managing Employee.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    
    @Inject
    private EmployeeRepository employeeRepository;
    
    @Inject
    private EmployeeMapper employeeMapper;
    
    /**
     * Save a employee.
     * @return the persisted entity
     */
    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        log.debug("Request to save Employee : {}", employeeDTO);
        Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
        employee = employeeRepository.save(employee);
        EmployeeDTO result = employeeMapper.employeeToEmployeeDTO(employee);
        return result;
    }

    /**
     *  get all the employees.
     *  @return the list of entities
     */
    public Page<Employee> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        Page<Employee> result = employeeRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one employee by id.
     *  @return the entity
     */
    public EmployeeDTO findOne(String id) {
        log.debug("Request to get Employee : {}", id);
        Employee employee = employeeRepository.findOne(id);
        EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(employee);
        return employeeDTO;
    }

    /**
     *  delete the  employee by id.
     */
    public void delete(String id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.delete(id);
    }
}
