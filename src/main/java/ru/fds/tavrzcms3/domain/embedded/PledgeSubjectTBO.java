package ru.fds.tavrzcms3.domain.embedded;

import lombok.*;
import ru.fds.tavrzcms3.dictionary.TypeOfTBO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class PledgeSubjectTBO{

	@Positive(message = "Значение должно быть больше нуля")
	@Column(name ="count_of_tbo", table = "pledge_tbo")
	private int countOfTBO;

	@Positive(message = "Значение должно быть больше нуля")
	@Column(name ="carrying_amount", table = "pledge_tbo")
	private double carryingAmount;

	@NotNull
	@Convert(converter = TypeOfTBO.Converter.class)
	@Column(name ="type_of_tbo", table = "pledge_tbo")
	private TypeOfTBO typeOfTBO;
}
