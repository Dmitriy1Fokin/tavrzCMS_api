package ru.fds.tavrzcms3.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@SequenceGenerator(name = "employee_sequence", sequenceName = "employee_employee_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_sequence")
	@Column(name ="employee_id")
	private Long employeeId;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="surname")
	private String surname;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="name")
	private String name;
	
	@Column(name ="patronymic")
	private String patronymic;
}
