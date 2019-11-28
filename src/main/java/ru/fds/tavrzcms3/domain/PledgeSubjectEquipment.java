package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.dictionary.TypeOfEquip;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "pledge_equipment")
public class PledgeSubjectEquipment extends PledgeSubject {

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="brand")
	private String brand;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="model")
	private String model;

	@Column(name ="serial_number")
	private String serialNum;

	@Min(value = 1900, message = "Неверное значение")
	@Max(value = 2100, message = "Неверное значение")
	@Length(min = 4, max = 4, message = "Неверное значение")
	@Column(name ="year_of_manufacture")
	private Integer yearOfManufacture;
	
	@Column(name ="inventory_number")
	private String inventoryNum;

	@Convert(converter = TypeOfEquip.Converter.class)
	@Column(name ="type_of_equipment")
	private TypeOfEquip typeOfEquipment;

	@Positive(message = "Значение должно быть больше нуля")
	@Column(name = "productivity")
	private Double productivity;

	@Column(name = "type_of_productivity")
	private String typeOfProductivity;

	@Valid
	@OneToOne(mappedBy = "pledgeSubjectEquipment")
	@JsonIgnore
	private PledgeSubject pledgeSubject;

	public PledgeSubjectEquipment(){
		super.setTypeOfCollateral(TypeOfCollateral.EQUIPMENT);
	}

	@Override
	public String toString() {
		return "PledgeSubjectEquipment{" +
				"brand='" + brand + '\'' +
				", model='" + model + '\'' +
				", serialNum='" + serialNum + '\'' +
				", yearOfManufacture=" + yearOfManufacture +
				", inventoryNum='" + inventoryNum + '\'' +
				", typeOfquipment='" + typeOfEquipment + '\'' +
				'}';
	}
}
