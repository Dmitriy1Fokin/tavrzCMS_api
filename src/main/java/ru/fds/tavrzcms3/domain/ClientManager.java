package ru.fds.tavrzcms3.domain;
import lombok.*;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
	
	@Column(name = "surname")
	private String surname;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "patronymic")
	private String patronymic;

	@Singular
	@OneToMany(mappedBy = "clientManager")
	private List<Client> clients;

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
