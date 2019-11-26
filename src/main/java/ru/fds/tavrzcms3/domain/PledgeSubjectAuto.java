package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.fds.tavrzcms3.dictionary.TypeOfAuto;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "pledge_auto")
public class PledgeSubjectAuto extends PledgeSubject {

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="brand")
	private String brand;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="model")
	private String model;

	@Pattern(regexp = "^$|[0-9A-Z]{17}",
			message = "Неверное значение")
	@Column(name ="vin")
	private String vin;

	@Column(name ="num_of_engine")
	private String numOfEngine;
	
	@Column(name ="num_of_pts")
	private String numOfPTS;

	@Min(value = 1000, message = "Неверное значение")
	@Max(value = 9999, message = "Неверное значение")
	@Column(name ="year_of_manufacture")
	private Integer yearOfManufacture;
	
	@Column(name ="inventory_number")
	private String inventoryNum;

	@Convert(converter = TypeOfAuto.Converter.class)
	@Column(name ="type_of_auto")
	private TypeOfAuto typeOfAuto;

	@Positive(message = "Значение должно быть больше нуля")
	@Column(name ="horsepower")
	private Double horsepower;

	@Valid
	@OneToOne(mappedBy = "pledgeSubjectAuto")
	@JsonIgnore
	private PledgeSubject pledgeSubject;

	public PledgeSubjectAuto(){
		super.setTypeOfCollateral(TypeOfCollateral.AUTO);
	}

	@Override
	public String toString() {
		return "PledgeSubjectAuto{" +
				"brand='" + brand + '\'' +
				", model='" + model + '\'' +
				", vin='" + vin + '\'' +
				", numOfEngine='" + numOfEngine + '\'' +
				", numOfPTS='" + numOfPTS + '\'' +
				", yearOfManufacture=" + yearOfManufacture +
				", inventoryNum='" + inventoryNum + '\'' +
				", typeOfAuto='" + typeOfAuto + '\'' +
				'}';
	}
}
