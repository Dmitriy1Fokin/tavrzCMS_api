package ru.fds.tavrzcms3.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.fds.tavrzcms3.dictionary.TypeOfSecurities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class PledgeSubjectSecurities{

	@NotNull(message = "Обязательно для заполнения")
	@PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="nominal_value", table = "pledge_securities")
	private double nominalValue;

	@NotNull(message = "Обязательно для заполнения")
	@PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="actual_value", table = "pledge_securities")
	private double actualValue;

	@Convert(converter = TypeOfSecurities.Converter.class)
	@Column(name ="type_of_securities", table = "pledge_securities")
	private TypeOfSecurities typeOfSecurities;
}
