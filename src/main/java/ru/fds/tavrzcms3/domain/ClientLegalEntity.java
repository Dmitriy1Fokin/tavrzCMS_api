package ru.fds.tavrzcms3.domain;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "client_legal_entity")
public class ClientLegalEntity extends Client {

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="organizational_form")
	private String organizationalForm;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="name")
	private String name;

	@Pattern(regexp = "^$|[0-9]{10}",
			message = "Неверное значение")
	@Column(name = "inn")
	private  String inn;

	@Valid
	@OneToOne(mappedBy = "clientLegalEntity")
	private Client client;

	public ClientLegalEntity(){
		super.setTypeOfClient("юл");
	}

	public String getOrganizationalForm() {
		return organizationalForm;
	}

	public void setOrganizationalForm(String organizationalForm) {
		this.organizationalForm = organizationalForm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInn() {
		return inn;
	}

	public void setInn(String inn) {
		this.inn = inn;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return "ClientLegalEntity{" +
				"organizationalForm='" + organizationalForm + '\'' +
				", name='" + name + '\'' +
				", inn='" + inn + '\'' +
				", clientId=" + getClientId() +
				", typeOfClient='" + getTypeOfClient() + '\'' +
				'}';
	}
}
