package com.example.demo.service;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

      public Employee registerEmployee(Employee employee) {
        try {
            return employeeRepository.save(employee);
        } catch (DataIntegrityViolationException e) {
            // Handle the exception - for example, throw a custom exception or return an error response
            throw new RuntimeException("Username already exists.");
        }
    }
    public Employee findByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }
    public Employee saveEmployee(Employee employee) {
        throw new UnsupportedOperationException("Unimplemented method 'saveEmployee'");
    }
}
