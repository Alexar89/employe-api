package net.employee.api.service.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.employee.api.entity.Employee;
import net.employee.api.repository.EmployeeRepository;
import net.employee.api.service.IEmployeeService;


@Service
public class EmployeeService implements IEmployeeService {
	@Autowired
	private EmployeeRepository repoEmployee;
	
	/*Inserta un empleado*/
	@Override
	public void guardar(Employee employee) {
		repoEmployee.save(employee);
		
	}

	/*Metodo para eliminar un empleado por ID*/
	@Override
	public void eliminar(int id) {
		repoEmployee.deleteById(id);
		
	}
	
	/*Metodo para busqueda del empleado por ID*/
	@Override
	public Employee buscarPorId(Integer id) {
		Optional<Employee> optional = repoEmployee.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

}
