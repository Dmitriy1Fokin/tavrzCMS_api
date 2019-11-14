package ru.fds.tavrzcms3.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@SuperBuilder
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
