package ru.fds.tavrzcms3.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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

	@Singular
	@OneToMany(mappedBy = "client")
	private List<LoanAgreement> loanAgreements;

	@Singular
	@OneToMany(mappedBy = "client")
	private List<PledgeAgreement> pledgeAgreements;

	@Override
	public String toString() {
		return "Client{" +
				"clientId=" + clientId +
				", typeOfClient='" + typeOfClient + '\'' +
				'}';
	}
}
