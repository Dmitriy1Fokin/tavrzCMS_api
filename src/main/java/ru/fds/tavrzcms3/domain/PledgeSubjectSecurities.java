package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.dictionary.TypeOfSecurities;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "pledge_securities")
public class PledgeSubjectSecurities extends PledgeSubject{

	@NotNull(message = "Обязательно для заполнения")
	@PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="nominal_value")
	private double nominalValue;

	@NotNull(message = "Обязательно для заполнения")
	@PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="actual_value")
	private double actualValue;

	@Convert(converter = TypeOfSecurities.Converter.class)
	@Column(name ="type_of_securities")
	private TypeOfSecurities typeOfSecurities;

	@Valid
	@OneToOne(mappedBy = "pledgeSubjectSecurities")
	@JsonIgnore
	private PledgeSubject pledgeSubject;

	public PledgeSubjectSecurities(){
		super.setTypeOfCollateral(TypeOfCollateral.SECURITIES);
	}

	@Override
	public String toString() {
		return "PledgeSubjectSecurities{" +
				"nominalValue=" + nominalValue +
				", actualValue=" + actualValue +
				", typeOfSecurities='" + typeOfSecurities + '\'' +
				'}';
	}
}
