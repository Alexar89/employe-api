package net.employee.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.employee.api.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
