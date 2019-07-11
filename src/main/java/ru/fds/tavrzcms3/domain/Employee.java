package ru.fds.tavrzcms3.domain;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="employee_id")
	private long employeeId;
	
	@Column(name ="surname")
	private String surname;
	
	@Column(name ="name")
	private String name;
	
	@Column(name ="patronymic")
	private String patronymic;

	@OneToOne
	@JoinColumn(name = "login")
	private AppUser appUser;
	
	@OneToMany(mappedBy = "employee")
	private List<Client> clients;

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

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
