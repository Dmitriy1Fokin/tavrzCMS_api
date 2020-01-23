package ru.fds.tavrzcms3.domain.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fds.tavrzcms3.dictionary.TypeOfEquipment;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class PledgeSubjectEquipment{

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="brand_equip", table = "pledge_equipment")
	private String brandEquip;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="model_equip", table = "pledge_equipment")
	private String modelEquip;

	@Column(name ="serial_number")
	private String serialNum;

	@Min(value = 1900, message = "Неверное значение")
	@Max(value = 2100, message = "Неверное значение")
	@Column(name ="year_of_manufacture_equip", table = "pledge_equipment")
	private Integer yearOfManufactureEquip;

	@Column(name ="inventory_number_equip", table = "pledge_equipment")
	private String inventoryNumEquip;

	@NotNull
	@Convert(converter = TypeOfEquipment.Converter.class)
	@Column(name ="type_of_equipment", table = "pledge_equipment")
	private TypeOfEquipment typeOfEquipment;

	@Positive(message = "Значение должно быть больше нуля")
	@Column(name = "productivity", table = "pledge_equipment")
	private Double productivity;

	@Column(name = "type_of_productivity", table = "pledge_equipment")
	private String typeOfProductivity;
}

