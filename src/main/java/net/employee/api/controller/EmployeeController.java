package net.employee.api.controller;


import java.sql.Date;
import java.time.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.employee.api.entity.Employee;
import net.employee.api.service.IEmployeeService;


@RestController
@RequestMapping("/api")
public class EmployeeController {
	@Autowired
	private IEmployeeService serviceEmployee;
	
	/*Inserta un empleado si se cumplen las condiciones*/
	/*cuando uno de los campos obligatorios sea null o no se envie se generará un respuesta indicando que campo falla*/
	@PostMapping(value="/guardar",produces = MediaType.APPLICATION_JSON_VALUE)
	public Employee guardar(@RequestBody Employee employee){
		//validamos que todos los campos obligatirios esten diligenciados
		if(employee.getNombres()!="" && employee.getApellidos()!="" && employee.getFecha_ingreso()!=null
				&& employee.getSalario_base()!=null) {
			//guardamos el empleado
			serviceEmployee.guardar(employee);
			
		}
		
		return employee;
	}
	
	/*Modifica un empleado, si algun campo no esta presente, la API genera un mensaje de error que informa
	 * que campo esta ausente*/
	@PutMapping(value="/modificar",produces = MediaType.APPLICATION_JSON_VALUE)
	public Employee modificar(@RequestBody Employee employee){
		//validamos que todos los campos obligatirios esten diligenciados
		if(employee.getId()>0 &&  employee.getNombres()!="" && employee.getApellidos()!="" && employee.getFecha_ingreso()!=null
						&& employee.getSalario_base()!=null) {
			serviceEmployee.guardar(employee);
		}
		return employee;
		
	}
	
	/*Elimina un empleado por id*/
	@DeleteMapping(value="/eliminar/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public String eliminar(@PathVariable("id") int id){
		//valida que se envie un id valido
		serviceEmployee.eliminar(id);
		return "Registro Eliminado... :) ---<>";
	}
	
	// Consulta usuario por id
	@GetMapping(value="/consultar/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public Employee consultarPorId(@PathVariable("id") int id) {
		return serviceEmployee.buscarPorId(id);
	}
	// metodo para calcular el salario de acuerdo a mes y dia del id del empleado, se debe enviar el parametro
	//por url /calculos/{id}/{mes}/{ano}
	@PostMapping(value="/calculos/{id}/{mes}/{ano}",produces = MediaType.APPLICATION_JSON_VALUE)
	public String calculosSalario(@PathVariable("id") int id, @PathVariable("mes") int mes, @PathVariable("ano") int ano) {
		//Variables locales necesarias para el calculo:
		int dias=30;
		Double salario=0.0;
		Double salarioDiario=0.0;
		Double salarioCalculado=0.0;
		String fecha_ingreso;
		String fecha_retiro;
		int dia_fecha_ingreso;
		int mes_fecha_ingreso;
		int ano_fecha_ingreso;
		int dia_fecha_retiro;
		int mes_fecha_retiro;
		int ano_fecha_retiro;
		String resultado="";
		// creamos un employee1
		Employee employee1 = new Employee();
		//Consultamos el employee y lo guardamos
		employee1 = serviceEmployee.buscarPorId(id);
		salario = employee1.getSalario_base();
		salarioDiario=salario/dias;
		//guardamos las fechas de ingreso y retiro
		fecha_ingreso=employee1.getFecha_ingreso().toString();
		
		//calculamos dia, año y mes de las fechas obtenidas
		if(employee1.getFecha_retiro()!=null) {
			fecha_retiro=employee1.getFecha_retiro().toString();
			LocalDate currentDate2 = LocalDate.parse(fecha_retiro);
			dia_fecha_retiro = currentDate2.getDayOfMonth(); 
			mes_fecha_retiro = currentDate2.getMonthValue(); 
			ano_fecha_retiro = currentDate2.getYear(); 
		} else {
			dia_fecha_retiro=0;
			mes_fecha_retiro=0;
			ano_fecha_retiro=0;
		}
		
		//la fecha de ingreso nunca es null
		LocalDate currentDate1 = LocalDate.parse(fecha_ingreso);
	    // dia ingreso 
	    dia_fecha_ingreso = currentDate1.getDayOfMonth(); 
	    // mes ingreso
	    mes_fecha_ingreso = currentDate1.getMonthValue(); 
	    // Año ingreso
	    ano_fecha_ingreso = currentDate1.getYear(); 
	   
	
		//Calculamos los días de salario a pagar
		if(mes>0 && mes<13 && ano>=ano_fecha_ingreso && ano<=ano_fecha_retiro) {
			//evaluamos extremos del ingreso y egreso
			if(mes==mes_fecha_ingreso && ano==ano_fecha_ingreso) {
				dias = 30-dia_fecha_ingreso;
				salarioCalculado = salarioDiario*(dias);
				// generamos resultado
				resultado = "{\n salario:"+salarioCalculado+",\n días Pagados:"+(dias)+"\n}";
				return resultado;
			}else if(mes==mes_fecha_retiro && ano==ano_fecha_retiro) {
				dias = dia_fecha_retiro;
				salarioCalculado = salarioDiario*(dias);
				// generamos resultado
				resultado = "{\n salario:"+salarioCalculado+",\n días Pagados:"+(dias)+"\n}";
				return resultado;
			//evaluamos el resto de casos cuando no estamos en los extremos
			}else if(ano>ano_fecha_ingreso && ano<ano_fecha_retiro && mes>mes_fecha_ingreso && mes<mes_fecha_retiro) {
				dias = 30;
				salarioCalculado = salarioDiario*(dias);
				// generamos resultado
				resultado = "{\n salario:"+salarioCalculado+",\n días Pagados:"+(dias)+"\n}";
				return resultado;
			}else if(ano<=ano_fecha_retiro && mes<mes_fecha_retiro) {
				dias = 30;
				salarioCalculado = salarioDiario*(dias);
				// generamos resultado
				resultado = "{\n salario:"+salarioCalculado+",\n días Pagados:"+(dias)+"\n}";
				return resultado;
				
			}else{
				salarioCalculado=0.0;
				resultado = "{\n salario:"+salarioCalculado+",\n días Pagados:0.0\n}";
				return resultado;
			}
			
		// si el año de retiro es 0 por que la fecha de retiro es null
		}else if(ano_fecha_retiro==0){
			//evaluamos extremos del ingreso y egreso
			if(mes==mes_fecha_ingreso && ano==ano_fecha_ingreso) {
				dias = 30-dia_fecha_ingreso;
				salarioCalculado = salarioDiario*(dias);
				// generamos resultado
				resultado = "{\n salario:"+salarioCalculado+",\n días Pagados:"+(dias)+"\n}";
				return resultado;
			}else if(ano>ano_fecha_ingreso) {
				dias = 30;
				salarioCalculado = salarioDiario*(dias);
				// generamos resultado
				resultado = "{\n salario:"+salarioCalculado+",\n días Pagados:"+(dias)+"\n}";
				return resultado;
			}else {
				salarioCalculado=0.0;
				resultado = "{\n salario:"+salarioCalculado+",\n días Pagados:0.0\n}";
				return resultado;
			}
			
		}else {
			salarioCalculado=0.0;
			resultado = "{\n salario:"+salarioCalculado+",\n días Pagados:0.0\n}";
			return resultado;
		}
		

	}
	
	/**
	 * Personalizamos el Data Binding para todas las propiedades de tipo Date
	 * @param webDataBinder
	 */
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	

}
