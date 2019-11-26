package ru.fds.tavrzcms3.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@SuperBuilder
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
		super.setTypeOfClient(TypeOfClient.INDIVIDUAL);

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
