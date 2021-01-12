package net.employee.api.service;

import java.util.List;

import net.employee.api.entity.Employee;

public interface IEmployeeService {
	
	Employee buscarPorId(Integer id);
	void guardar(Employee employee);
	void eliminar(int id);
	
	
}
