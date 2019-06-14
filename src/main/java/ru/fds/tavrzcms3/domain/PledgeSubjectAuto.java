package ru.fds.tavrzcms3.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pledge_auto")
public class PledgeSubjectAuto extends PledgeSubject {
	
	@Column(name ="brand")
	private String brand;
	
	@Column(name ="model")
	private String model;
	
	@Column(name ="vin")
	private String vin;
	
	@Column(name ="num_of_engine")
	private String numOfEngine;
	
	@Column(name ="num_of_PTS")
	private String numOfPTS;
	
	@Column(name ="year_of_manufacture")
	private int yearOfManufacture;
	
	@Column(name ="inventory_number")
	private String inventoryNum;
	
	@Column(name ="type_of_auto")
	private String typeOfAuto;
	
	@OneToOne(mappedBy = "pledgeSubjectAuto")
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

	public String getTypeOfAuto() {
		return typeOfAuto;
	}

	public void setTypeOfAuto(String typeOfAuto) {
		this.typeOfAuto = typeOfAuto;
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
				", pledgeSubject=" + pledgeSubject +
				'}';
	}
}
