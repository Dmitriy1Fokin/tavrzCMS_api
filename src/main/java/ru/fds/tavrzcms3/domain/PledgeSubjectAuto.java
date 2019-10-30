package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.*;

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

	@Pattern(regexp = "бульдозер|экскаватор|прицеп|погрузчик|кран|дорожно-строительная|комбайн|трактор|" +
			"пассажирский транспорт|грузовой транспорт|легковой транспорт|ж/д транспорт|иное",
			message = "Возможные варианты: бульдозе, экскаватор, прицеп, погрузчик, кран, дорожно-строительная, " +
					"комбайн, трактор, пассажирский транспорт, грузовой транспорт, легковой транспорт, ж/д транспорт, иное")
	@Column(name ="type_of_auto")
	private String typeOfAuto;

	@Positive(message = "Значение должно быть больше нуля")
	@Column(name ="horsepower")
	private Double horsepower;

	@Valid
	@OneToOne(mappedBy = "pledgeSubjectAuto")
	@JsonIgnore
	private PledgeSubject pledgeSubject;

	public PledgeSubjectAuto(){
		super.setTypeOfCollateral("Авто/спецтехника");
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getNumOfEngine() {
		return numOfEngine;
	}

	public void setNumOfEngine(String numOfEngine) {
		this.numOfEngine = numOfEngine;
	}

	public String getNumOfPTS() {
		return numOfPTS;
	}

	public void setNumOfPTS(String numOfPTS) {
		this.numOfPTS = numOfPTS;
	}

	public Integer getYearOfManufacture() {
		return yearOfManufacture;
	}

	public void setYearOfManufacture(Integer yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}

	public String getInventoryNum() {
		return inventoryNum;
	}

	public void setInventoryNum(String inventoryNum) {
		this.inventoryNum = inventoryNum;
	}

	public String getTypeOfAuto() {
		return typeOfAuto;
	}

	public void setTypeOfAuto(String typeOfAuto) {
		this.typeOfAuto = typeOfAuto;
	}

	public Double getHorsepower() {
		return horsepower;
	}

	public void setHorsepower(Double horsepower) {
		this.horsepower = horsepower;
	}

	public PledgeSubject getPledgeSubject() {
		return pledgeSubject;
	}

	public void setPledgeSubject(PledgeSubject pledgeSubject) {
		this.pledgeSubject = pledgeSubject;
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
