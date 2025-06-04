package com.naga.multi_database.controller.employee;


import com.finance.commons.exception.dto.Error;
import com.finance.commons.exception.dto.Success;
import com.naga.multi_database.services.employee.EmployeeService;
import com.naga.multi_database.sqldb.dto.EmployeeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "CRUD REST API for Employee",
description = "CRUD REST API to create,update,delete & fetch from DB")
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        Optional<EmployeeDTO> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create Employee REST API",
    description = "REST API to create new employee.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "HTTP status CREATED"),
            @ApiResponse(responseCode = "500",
            description = "HTTP status for Internal server error.",
            content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/create")
    public EmployeeDTO createEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        return employeeService.saveEmployee(employeeDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        try {
            EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
            return ResponseEntity.ok(updatedEmployee);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test")
    public ResponseEntity<Success> getTest(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
