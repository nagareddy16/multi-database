package com.naga.multi_database.services.employee;


import com.naga.multi_database.sqldb.dto.EmployeeDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface EmployeeService {

    List<EmployeeDTO> getAllEmployees();
    Optional<EmployeeDTO> getEmployeeById(Long id);
    EmployeeDTO saveEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    void deleteEmployee(Long id);
}
