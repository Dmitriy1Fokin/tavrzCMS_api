package ru.fds.tavrzcms3.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.embedded.ClientIndividual;
import ru.fds.tavrzcms3.domain.embedded.ClientLegalEntity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "client_prime")
@SecondaryTables({
		@SecondaryTable(name = "client_individual", pkJoinColumns = @PrimaryKeyJoinColumn(name = "client_id")),
		@SecondaryTable(name = "client_legal_entity", pkJoinColumns = @PrimaryKeyJoinColumn(name = "client_id"))
})
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="client_id")
	private Long clientId;

	@NotNull(message = "Обязательно для заполнения")
	@Convert(converter = TypeOfClient.Converter.class)
	@Column(name = "type_of_client")
	private TypeOfClient typeOfClient;

	@NotNull(message = "Обязательно для заполнения")
	@ManyToOne()
	@JoinColumn(name = "client_manager_id")
	private ClientManager clientManager;

	@NotNull(message = "Обязательно для заполнения")
	@ManyToOne()
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@Valid
    @Embedded
	private ClientIndividual clientIndividual;

	@Valid
    @Embedded
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
                ", typeOfClient=" + typeOfClient +
                ", clientIndividual=" + clientIndividual +
                ", clientLegalEntity=" + clientLegalEntity +
                '}';
    }
}
