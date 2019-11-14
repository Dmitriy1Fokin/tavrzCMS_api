package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "pledge_realty_building")
public class PledgeSubjectRealtyBuilding extends PledgeSubjectRealty {

	@NotNull(message = "Обязательно для заполнения")
	@Min(value = 1, message = "Неверное значение")
	@Max(value = 100, message = "Неверное значение")
	@Column(name ="readiness_degree")
	private int readinessDegree;

	@NotNull(message = "Обязательно для заполнения")
	@Min(value = 1000, message = "Неверное значение")
	@Max(value = 9999, message = "Неверное значение")
	@Column(name ="year_of_construction")
	private int yearOfConstruction;

	@Valid
	@OneToOne(mappedBy = "pledgeSubjectRealtyBuilding")
	@JsonIgnore
	private PledgeSubjectRealty pledgeSubjectRealty;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "market_segment_id")
	@JsonIgnore
	private MarketSegment marketSegment;

	public PledgeSubjectRealtyBuilding(){
		super.setTypeOfCollateral("Недвижимость - здание/сооружение");
	}

	@Override
	public String toString() {
		return "PledgeSubjectRealtyBuilding{" +
				"readinessDegree=" + readinessDegree +
				", yearOfConstruction=" + yearOfConstruction +
				'}';
	}
}
