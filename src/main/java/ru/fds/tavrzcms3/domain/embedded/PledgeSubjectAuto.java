package ru.fds.tavrzcms3.domain.embedded;

import lombok.*;
import ru.fds.tavrzcms3.dictionary.TypeOfAuto;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class PledgeSubjectAuto{

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="brand_auto", table = "pledge_auto")
	private String brandAuto;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="model_auto", table = "pledge_auto")
	private String modelAuto;

	@Pattern(regexp = "^$|[0-9A-Z]{17}",
			message = "Неверное значение")
	@Column(name ="vin", table = "pledge_auto")
	private String vin;

	@Column(name ="num_of_engine", table = "pledge_auto")
	private String numOfEngine;
	
	@Column(name ="num_of_pts")
	private String numOfPTS;

	@Min(value = 1900, message = "Неверное значение")
	@Max(value = 2100, message = "Неверное значение")
	@Column(name ="year_of_manufacture_auto", table = "pledge_auto")
	private Integer yearOfManufactureAuto;
	
	@Column(name ="inventory_number_auto")
	private String inventoryNumAuto;

	@NotNull
	@Convert(converter = TypeOfAuto.Converter.class)
	@Column(name ="type_of_auto", table = "pledge_auto")
	private TypeOfAuto typeOfAuto;

	@Positive(message = "Значение должно быть больше нуля")
	@Column(name ="horsepower", table = "pledge_auto")
	private Double horsepower;
}
