package com.naga.multi_database.sqldb.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "EmployeeDto",
description = "Scheme to hold the Employee information")
public record EmployeeDTO(Long id, String firstName, String lastName, String email) {}

