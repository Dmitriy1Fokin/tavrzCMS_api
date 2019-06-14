package ru.fds.tavrzcms3.domain;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "client_manager")
public class ClientManager {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="client_manager_id")
	private long clientManagerId;
	
	@Column(name = "surname")
	private String surname;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "patronymic")
	private String patronymic;
	
	@OneToMany(mappedBy = "clientManager")
	private List<Client> clients;

	public long getClientManagerId() {
		return clientManagerId;
	}

	public void setClientManagerId(long clientManagerId) {
		this.clientManagerId = clientManagerId;
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

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

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
