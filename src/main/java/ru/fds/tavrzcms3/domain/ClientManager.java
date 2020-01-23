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
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "client_manager")
public class ClientManager {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="client_manager_id")
	private Long clientManagerId;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name = "surname")
	private String surname;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name = "name")
	private String name;
	
	@Column(name = "patronymic")
	private String patronymic;

	@Override
	public String toString() {
		return "ClientManager{" +
				"clientManagerId=" + clientManagerId +
				", surname='" + surname + '\'' +
				", name='" + name + '\'' +
				", patronymic='" + patronymic + '\'' +
				'}';
	}
}
