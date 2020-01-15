package ru.fds.tavrzcms3.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@OneToOne
	@JoinColumn(name = "login")
	private AppUser appUser;

	@Singular
	@OneToMany(mappedBy = "employee")
	private List<Client> clients;

	@Override
	public String toString() {
		return "Employee{" +
				"employeeId=" + employeeId +
				", surname='" + surname + '\'' +
				", name='" + name + '\'' +
				", patronymic='" + patronymic + '\'' +
				", appUser=" + appUser +
				'}';
	}
}
