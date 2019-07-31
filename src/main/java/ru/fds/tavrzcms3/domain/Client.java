package ru.fds.tavrzcms3.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "client_prime")
@Inheritance(strategy = InheritanceType.JOINED)
public class Client {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="client_id")
	private long clientId;
	
	@Column(name = "type_of_client")
	private String typeOfClient;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_manager_id")
	private ClientManager clientManager;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@OneToOne
	@JoinColumn(name = "client_id")
	private ClientIndividual clientIndividual;
	
	@OneToOne
	@JoinColumn(name = "client_id")
	private ClientLegalEntity ClientLegalEntity;
	
	@OneToMany(mappedBy = "loaner")
	private List<LoanEgreement> loanEgreements;

	@OneToMany(mappedBy = "pledgor")
	private List<PledgeEgreement> pledgeEgreements;

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public String getTypeOfClient() {
		return typeOfClient;
	}

	public void setTypeOfClient(String typeOfClient) {
		this.typeOfClient = typeOfClient;
	}

	public ClientManager getClientManager() {
		return clientManager;
	}

	public void setClientManager(ClientManager clientManager) {
		this.clientManager = clientManager;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public ClientIndividual getClientIndividual() {
		return clientIndividual;
	}

	public void setClientIndividual(ClientIndividual clientIndividual) {
		this.clientIndividual = clientIndividual;
	}

	public ClientLegalEntity getClientLegalEntity() {
		return ClientLegalEntity;
	}

	public void setClientLegalEntity(ClientLegalEntity clientLegalEntity) {
		ClientLegalEntity = clientLegalEntity;
	}

	public List<LoanEgreement> getLoanEgreements() {
		return loanEgreements;
	}

	public void setLoanEgreements(List<LoanEgreement> loanEgreements) {
		this.loanEgreements = loanEgreements;
	}

	public List<PledgeEgreement> getPledgeEgreements() {
		return pledgeEgreements;
	}

	public void setPledgeEgreements(List<PledgeEgreement> pledgeEgreements) {
		this.pledgeEgreements = pledgeEgreements;
	}

	@Override
	public String toString() {
		return "Client{" +
				"clientId=" + clientId +
				", typeOfClient='" + typeOfClient + '\'' +
//				", clientManager=" + clientManager +
//				", employee=" + employee +
				'}';
	}
}
