package ru.fds.tavrzcms3.domain.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class ClientIndividual{

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="surname", table = "client_individual")
	private String surname;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="name", table = "client_individual")
	private String name;
	
	@Column(name ="patronymic", table = "client_individual")
	private String patronymic;

	@Pattern(regexp = "^$|[0-9]{4} [0-9]{6}",
			message = "Неверное значение")
	@Column(name = "pasport_number", table = "client_individual")
	private String pasportNum;
}
