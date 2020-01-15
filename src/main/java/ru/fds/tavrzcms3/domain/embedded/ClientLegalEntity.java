package ru.fds.tavrzcms3.domain.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class ClientLegalEntity{

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="organizational_form", table = "client_legal_entity")
	private String organizationalForm;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="name", table = "client_legal_entity")
	private String name;

	@Pattern(regexp = "^$|[0-9]{10}",
			message = "Неверное значение")
	@Column(name = "inn", table = "client_legal_entity")
	private  String inn;
}
