package com.ajlopez.myapp.web.rest;

import com.ajlopez.myapp.Application;
import com.ajlopez.myapp.domain.Employee;
import com.ajlopez.myapp.repository.EmployeeRepository;
import com.ajlopez.myapp.service.EmployeeService;
import com.ajlopez.myapp.web.rest.dto.EmployeeDTO;
import com.ajlopez.myapp.web.rest.mapper.EmployeeMapper;

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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EmployeeResource REST controller.
 *
 * @see EmployeeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EmployeeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";

    private static final LocalDate DEFAULT_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_SALARY = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALARY = new BigDecimal(2);

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private EmployeeMapper employeeMapper;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmployeeResource employeeResource = new EmployeeResource();
        ReflectionTestUtils.setField(employeeResource, "employeeService", employeeService);
        ReflectionTestUtils.setField(employeeResource, "employeeMapper", employeeMapper);
        this.restEmployeeMockMvc = MockMvcBuilders.standaloneSetup(employeeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        employeeRepository.deleteAll();
        employee = new Employee();
        employee.setName(DEFAULT_NAME);
        employee.setAddress(DEFAULT_ADDRESS);
        employee.setCreated(DEFAULT_CREATED);
        employee.setSalary(DEFAULT_SALARY);
    }

    @Test
    public void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(employee);

        restEmployeeMockMvc.perform(post("/api/employees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
                .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employees.get(employees.size() - 1);
        assertThat(testEmployee.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmployee.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testEmployee.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testEmployee.getSalary()).isEqualTo(DEFAULT_SALARY);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setName(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(employee);

        restEmployeeMockMvc.perform(post("/api/employees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
                .andExpect(status().isBadRequest());

        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkSalaryIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setSalary(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(employee);

        restEmployeeMockMvc.perform(post("/api/employees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
                .andExpect(status().isBadRequest());

        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.save(employee);

        // Get all the employees
        restEmployeeMockMvc.perform(get("/api/employees?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
                .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.intValue())));
    }

    @Test
    public void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.save(employee);

        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(employee.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY.intValue()));
    }

    @Test
    public void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateEmployee() throws Exception {
        // Initialize the database
        employeeRepository.save(employee);

		int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        employee.setName(UPDATED_NAME);
        employee.setAddress(UPDATED_ADDRESS);
        employee.setCreated(UPDATED_CREATED);
        employee.setSalary(UPDATED_SALARY);
        EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(employee);

        restEmployeeMockMvc.perform(put("/api/employees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
                .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employees.get(employees.size() - 1);
        assertThat(testEmployee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmployee.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEmployee.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testEmployee.getSalary()).isEqualTo(UPDATED_SALARY);
    }

    @Test
    public void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.save(employee);

		int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Get the employee
        restEmployeeMockMvc.perform(delete("/api/employees/{id}", employee.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(databaseSizeBeforeDelete - 1);
    }
}
