package ru.fds.tavrzcms3.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pledge_equipment")
public class PledgeSubjectEquipment extends PledgeSubject {

	@Column(name ="brand")
	private String brand;
	
	@Column(name ="model")
	private String model;
	
	@Column(name ="serial_number")
	private String serialNum;
	
	@Column(name ="year_of_manufacture")
	private int yearOfManufacture;
	
	@Column(name ="inventory_number")
	private String inventoryNum;
	
	@Column(name ="type_of_equipment")
	private String typeOfquipment;
	
	@OneToOne(mappedBy = "pledgeSubjectEquipment")
	private PledgeSubject pledgeSubject;

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

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public int getYearOfManufacture() {
		return yearOfManufacture;
	}

	public void setYearOfManufacture(int yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}

	public String getInventoryNum() {
		return inventoryNum;
	}

	public void setInventoryNum(String inventoryNum) {
		this.inventoryNum = inventoryNum;
	}

	public String getTypeOfquipment() {
		return typeOfquipment;
	}

	public void setTypeOfquipment(String typeOfquipment) {
		this.typeOfquipment = typeOfquipment;
	}

	public PledgeSubject getPledgeSubject() {
		return pledgeSubject;
	}

	public void setPledgeSubject(PledgeSubject pledgeSubject) {
		this.pledgeSubject = pledgeSubject;
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
				", pledgeSubject=" + pledgeSubject +
				'}';
	}
}
