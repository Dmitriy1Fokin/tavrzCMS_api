package ru.fds.tavrzcms3.domain;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "client_prime")
@Inheritance(strategy = InheritanceType.JOINED)
public class Client {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="client_id")
	private long clientId;

	@Pattern(regexp = "юл|фл", message = "Возможные варианты: юл, фл")
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
	private ClientLegalEntity clientLegalEntity;
	
	@OneToMany(mappedBy = "client")
	private List<LoanAgreement> loanAgreements;

	@OneToMany(mappedBy = "client")
	private List<PledgeAgreement> pledgeAgreements;

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
		return clientLegalEntity;
	}

	public void setClientLegalEntity(ClientLegalEntity clientLegalEntity) {
		this.clientLegalEntity = clientLegalEntity;
	}

	public List<LoanAgreement> getLoanAgreements() {
		return loanAgreements;
	}

	public void setLoanAgreements(List<LoanAgreement> loanAgreements) {
		this.loanAgreements = loanAgreements;
	}

	public List<PledgeAgreement> getPledgeAgreements() {
		return pledgeAgreements;
	}

	public void setPledgeAgreements(List<PledgeAgreement> pledgeAgreements) {
		this.pledgeAgreements = pledgeAgreements;
	}

	@Override
	public String toString() {
		return "Client{" +
				"clientId=" + clientId +
				", typeOfClient='" + typeOfClient + '\'' +
				'}';
	}
}
