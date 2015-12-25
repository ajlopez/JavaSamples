package com.ajlopez.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ajlopez.myapp.domain.Employee;
import com.ajlopez.myapp.service.EmployeeService;
import com.ajlopez.myapp.web.rest.util.HeaderUtil;
import com.ajlopez.myapp.web.rest.util.PaginationUtil;
import com.ajlopez.myapp.web.rest.dto.EmployeeDTO;
import com.ajlopez.myapp.web.rest.mapper.EmployeeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Employee.
 */
@RestController
@RequestMapping("/api")
public class EmployeeResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeResource.class);
        
    @Inject
    private EmployeeService employeeService;
    
    @Inject
    private EmployeeMapper employeeMapper;
    
    /**
     * POST  /employees -> Create a new employee.
     */
    @RequestMapping(value = "/employees",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) throws URISyntaxException {
        log.debug("REST request to save Employee : {}", employeeDTO);
        if (employeeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employee", "idexists", "A new employee cannot already have an ID")).body(null);
        }
        EmployeeDTO result = employeeService.save(employeeDTO);
        return ResponseEntity.created(new URI("/api/employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("employee", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employees -> Updates an existing employee.
     */
    @RequestMapping(value = "/employees",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeDTO> updateEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) throws URISyntaxException {
        log.debug("REST request to update Employee : {}", employeeDTO);
        if (employeeDTO.getId() == null) {
            return createEmployee(employeeDTO);
        }
        EmployeeDTO result = employeeService.save(employeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("employee", employeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employees -> get all the employees.
     */
    @RequestMapping(value = "/employees",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Employees");
        Page<Employee> page = employeeService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/employees");
        return new ResponseEntity<>(page.getContent().stream()
            .map(employeeMapper::employeeToEmployeeDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /employees/:id -> get the "id" employee.
     */
    @RequestMapping(value = "/employees/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable String id) {
        log.debug("REST request to get Employee : {}", id);
        EmployeeDTO employeeDTO = employeeService.findOne(id);
        return Optional.ofNullable(employeeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employees/:id -> delete the "id" employee.
     */
    @RequestMapping(value = "/employees/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        log.debug("REST request to delete Employee : {}", id);
        employeeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employee", id.toString())).build();
    }
}
