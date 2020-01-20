package ru.fds.tavrzcms3.domain.embedded;

import lombok.*;
import ru.fds.tavrzcms3.dictionary.TypeOfEquipment;

import javax.persistence.*;
import javax.validation.constraints.*;

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

