package com.ajlopez.myapp.repository;

import com.ajlopez.myapp.domain.Employee;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Employee entity.
 */
public interface EmployeeRepository extends MongoRepository<Employee,String> {

}
