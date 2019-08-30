package ru.fds.tavrzcms3.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pledge_securities")
public class PledgeSubjectSecurities extends PledgeSubject{
	
	@Column(name ="nominal_value")
	private double nominalValue;
	
	@Column(name ="actual_value")
	private double actualValue;
	
	@Column(name ="type_of_securities")
	private String typeOfSecurities;
	
	@OneToOne(mappedBy = "pledgeSubjectSecurities")
	private PledgeSubject pledgeSubject;

	public double getNominalValue() {
		return nominalValue;
	}

	public void setNominalValue(double nominalValue) {
		this.nominalValue = nominalValue;
	}

	public double getActualValue() {
		return actualValue;
	}

	public void setActualValue(double actualValue) {
		this.actualValue = actualValue;
	}

	public String getTypeOfSecurities() {
		return typeOfSecurities;
	}

	public void setTypeOfSecurities(String typeOfSecurities) {
		this.typeOfSecurities = typeOfSecurities;
	}

	public PledgeSubject getPledgeSubject() {
		return pledgeSubject;
	}

	public void setPledgeSubject(PledgeSubject pledgeSubject) {
		this.pledgeSubject = pledgeSubject;
	}

	@Override
	public String toString() {
		return "PledgeSubjectSecurities{" +
				"nominalValue=" + nominalValue +
				", actualValue=" + actualValue +
				", typeOfSecurities='" + typeOfSecurities + '\'' +
				'}';
	}
}
