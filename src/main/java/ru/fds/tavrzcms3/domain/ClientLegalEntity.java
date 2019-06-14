package ru.fds.tavrzcms3.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "loaner_legal_entity")
public class ClientLegalEntity extends Client {
	
	@Column(name ="organizational_form")
	private String organizationalForm;
	
	@Column(name ="name")
	private String name;

	@Column(name = "inn")
	private  String inn;

	@OneToOne(mappedBy = "ClientLegalEntity")
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
				", client=" + client +
				'}';
	}
}
