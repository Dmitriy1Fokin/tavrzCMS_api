package ru.fds.tavrzcms3.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fds.tavrzcms3.dictionary.TypeOfClient;
import ru.fds.tavrzcms3.domain.embedded.ClientIndividual;
import ru.fds.tavrzcms3.domain.embedded.ClientLegalEntity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.SequenceGenerator;
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
	@SequenceGenerator(name = "client_id_seq", sequenceName = "client_prime_client_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_id_seq")
	@Column(name ="client_id")
	private Long clientId;

	@NotNull(message = "Обязательно для заполнения")
	@Convert(converter = TypeOfClient.Converter.class)
	@Column(name = "type_of_client")
	private TypeOfClient typeOfClient;

	@NotNull(message = "Обязательно для заполнения")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_manager_id")
	private ClientManager clientManager;

	@NotNull(message = "Обязательно для заполнения")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@Valid
    @Embedded
	private ClientIndividual clientIndividual;

	@Valid
    @Embedded
	private ClientLegalEntity clientLegalEntity;

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
