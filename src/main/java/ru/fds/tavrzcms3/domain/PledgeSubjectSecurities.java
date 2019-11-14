package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Data
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

	@Pattern(regexp = "доли в ук|акции|вексель|паи|",
			message = "Возможные варианты: доли в ук, акции, вексель, паи")
	@Column(name ="type_of_securities")
	private String typeOfSecurities;

	@Valid
	@OneToOne(mappedBy = "pledgeSubjectSecurities")
	@JsonIgnore
	private PledgeSubject pledgeSubject;

	public PledgeSubjectSecurities(){
		super.setTypeOfCollateral("Ценные бумаги");
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
