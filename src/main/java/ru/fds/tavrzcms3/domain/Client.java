package ru.fds.tavrzcms3.domain;

import lombok.*;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.embedded.ClientIndividual;
import ru.fds.tavrzcms3.domain.embedded.ClientLegalEntity;
import ru.fds.tavrzcms3.validate.validationgroup.Exist;
import ru.fds.tavrzcms3.validate.validationgroup.New;

import java.util.List;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

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

	@NotNull(groups = Exist.class)
	@Null(groups = New.class)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="client_id")
	private Long clientId;

	@NotNull
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
