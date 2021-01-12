package net.employee.api.entity;

//import java.util.Date;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="employee")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombres;
	private String apellidos;
	private Date fecha_ingreso;
	private Double salario_base;
	private Date fecha_retiro;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public Date getFecha_ingreso() {
		return fecha_ingreso;
	}
	public void setFecha_ingreso(Date fecha_ingreso) {
		this.fecha_ingreso = fecha_ingreso;
	}
	public Double getSalario_base() {
		return salario_base;
	}
	public void setSalario_base(Double salario_base) {
		this.salario_base = salario_base;
	}
	public Date getFecha_retiro() {
		return fecha_retiro;
	}
	public void setFecha_retiro(Date fecha_retiro) {
		this.fecha_retiro = fecha_retiro;
	}
	@Override
	public String toString() {
		return "Employee [id=" + id + ", nombres=" + nombres + ", apellidos=" + apellidos + ", fecha_ingreso="
				+ fecha_ingreso + ", salario_base=" + salario_base + ", fecha_retiro=" + fecha_retiro + "]";
	}
	
	
	
}
