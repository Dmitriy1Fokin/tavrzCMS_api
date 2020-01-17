package ru.fds.tavrzcms3.domain.embedded;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fds.tavrzcms3.dictionary.TypeOfVessel;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class PledgeSubjectVessel{

	@Pattern(regexp = "[0-9]{7}")
	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="imo", table = "pledge_vessel")
	private String imo;

	@Pattern(regexp = "^$|[0-9]{9}")
	@Column(name ="mmsi", table = "pledge_vessel")
	private String mmsi;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="flag", table = "pledge_vessel")
	private String flag;

	@NotNull
	@Convert(converter = TypeOfVessel.Converter.class)
	@Column(name ="vessel_type", table = "pledge_vessel")
	private TypeOfVessel vesselType;

	@NotNull(message = "Обязательно для заполнения")
	@Positive(message = "Значение должно быть больше нуля")
	@Column(name ="gross_tonnage", table = "pledge_vessel")
	private Integer grossTonnage;

	@NotNull(message = "Обязательно для заполнения")
	@Positive(message = "Значение должно быть больше нуля")
	@Column(name ="deadweight", table = "pledge_vessel")
	private Integer deadweight;

	@Min(value = 1900, message = "Неверное значение")
	@Max(value = 2100, message = "Неверное значение")
	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="year_built", table = "pledge_vessel")
	private Integer yearBuilt;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="status", table = "pledge_vessel")
	private String statusVessel;
}
