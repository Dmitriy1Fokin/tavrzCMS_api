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
import javax.validation.constraints.*;

@Data
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

	@Min(value = 1000, message = "Неверное значение")
	@Max(value = 9999, message = "Неверное значение")
	@Column(name ="year_of_manufacture")
	private Integer yearOfManufacture;
	
	@Column(name ="inventory_number")
	private String inventoryNum;

	@Pattern(regexp = "металлообработка|лесообработка|торговое|офисное|сети ито|рекламное|пищевое|автомобильное|азс|" +
			"химическое|измерительное|медицинское|нефте-газовое|карьерное и горное|подъемное|авиационное|строительое|" +
			"ресторанное|транспортировка|упаковачное|хранение|с/х назначения|иное",
			message = "Возможные варианты: металлообработка, лесообработка, торговое, офисное, сети ито, рекламное, " +
					"пищевое, автомобильное, азс, химическое, измерительное, медицинское, нефте-газовое, " +
					"карьерное и горное, подъемное, авиационное, строительое, ресторанное, транспортировка, упаковачное, " +
					"хранение, с/х назначения, иное")
	@Column(name ="type_of_equipment")
	private String typeOfquipment;

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
		super.setTypeOfCollateral("Оборудование");
	}

	@Override
	public String toString() {
		return "PledgeSubjectEquipment{" +
				"brand='" + brand + '\'' +
				", model='" + model + '\'' +
				", serialNum='" + serialNum + '\'' +
				", yearOfManufacture=" + yearOfManufacture +
				", inventoryNum='" + inventoryNum + '\'' +
				", typeOfquipment='" + typeOfquipment + '\'' +
				'}';
	}
}
