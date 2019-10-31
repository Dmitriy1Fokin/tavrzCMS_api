package ru.fds.tavrzcms3.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "client_individual")
public class ClientIndividual extends Client {

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="surname")
	private String surname;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="name")
	private String name;
	
	@Column(name ="patronymic")
	private String patronymic;

	@Pattern(regexp = "^$|[0-9]{4} [0-9]{6}",
			message = "Неверное значение")
	@Column(name = "pasport_number")
	private String pasportNum;

	@Valid
	@OneToOne(mappedBy = "clientIndividual")
	private Client client;

	public ClientIndividual(){
		super.setTypeOfClient("фл");

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

	public String getPasportNum() {
		return pasportNum;
	}

	public void setPasportNum(String pasportNum) {
		this.pasportNum = pasportNum;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return "ClientIndividual{" +
				"surname='" + surname + '\'' +
				", name='" + name + '\'' +
				", patronymic='" + patronymic + '\'' +
				", pasportNum='" + pasportNum + '\'' +
				"} ";
	}
}
