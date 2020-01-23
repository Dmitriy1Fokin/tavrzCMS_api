package ru.fds.tavrzcms3.domain.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fds.tavrzcms3.dictionary.TypeOfTBO;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
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
	private Integer countOfTBO;

	@Positive(message = "Значение должно быть больше нуля")
	@Column(name ="carrying_amount", table = "pledge_tbo")
	private Double carryingAmount;

	@NotNull
	@Convert(converter = TypeOfTBO.Converter.class)
	@Column(name ="type_of_tbo", table = "pledge_tbo")
	private TypeOfTBO typeOfTBO;
}
