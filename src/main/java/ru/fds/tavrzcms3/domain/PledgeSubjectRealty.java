package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "pledge_realty_prime")
@Inheritance(strategy = InheritanceType.JOINED)
public class PledgeSubjectRealty extends PledgeSubject {

	@NotNull(message = "Обязательно для заполнения")
	@PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="area")
	private double area;

	@Pattern(regexp = "[0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+",
			message = "Неверное значение")
	@Column(name ="cadastral_num")
	private String cadastralNum;
	
	@Column(name ="conditional_num")
	private String conditionalNum;

	@Valid
	@OneToOne(mappedBy = "pledgeSubjectRealty")
	@JsonIgnore
	private PledgeSubject pledgeSubject;
	
	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
	@JsonIgnore
	private PledgeSubjectRealtyBuilding pledgeSubjectRealtyBuilding;
	
	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
	@JsonIgnore
	private PledgeSubjectRealtyLandLease pledgeSubjectRealtyLandLease;
	
	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
	@JsonIgnore
	private PledgeSubjectRealtyLandOwnership pledgeSubjectRealtyLandOwnership;
	
	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
	@JsonIgnore
	private PledgeSubjectRealtyRoom pledgeSubjectRealtyRoom;

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public String getCadastralNum() {
		return cadastralNum;
	}

	public void setCadastralNum(String cadastralNum) {
		this.cadastralNum = cadastralNum;
	}

	public String getConditionalNum() {
		return conditionalNum;
	}

	public void setConditionalNum(String conditionalNum) {
		this.conditionalNum = conditionalNum;
	}

	public PledgeSubject getPledgeSubject() {
		return pledgeSubject;
	}

	public void setPledgeSubject(PledgeSubject pledgeSubject) {
		this.pledgeSubject = pledgeSubject;
	}

	public PledgeSubjectRealtyBuilding getPledgeSubjectRealtyBuilding() {
		return pledgeSubjectRealtyBuilding;
	}

	public void setPledgeSubjectRealtyBuilding(PledgeSubjectRealtyBuilding pledgeSubjectRealtyBuilding) {
		this.pledgeSubjectRealtyBuilding = pledgeSubjectRealtyBuilding;
	}

	public PledgeSubjectRealtyLandLease getPledgeSubjectRealtyLandLease() {
		return pledgeSubjectRealtyLandLease;
	}

	public void setPledgeSubjectRealtyLandLease(PledgeSubjectRealtyLandLease pledgeSubjectRealtyLandLease) {
		this.pledgeSubjectRealtyLandLease = pledgeSubjectRealtyLandLease;
	}

	public PledgeSubjectRealtyLandOwnership getPledgeSubjectRealtyLandOwnership() {
		return pledgeSubjectRealtyLandOwnership;
	}

	public void setPledgeSubjectRealtyLandOwnership(PledgeSubjectRealtyLandOwnership pledgeSubjectRealtyLandOwnership) {
		this.pledgeSubjectRealtyLandOwnership = pledgeSubjectRealtyLandOwnership;
	}

	public PledgeSubjectRealtyRoom getPledgeSubjectRealtyRoom() {
		return pledgeSubjectRealtyRoom;
	}

	public void setPledgeSubjectRealtyRoom(PledgeSubjectRealtyRoom pledgeSubjectRealtyRoom) {
		this.pledgeSubjectRealtyRoom = pledgeSubjectRealtyRoom;
	}

	@Override
	public String toString() {
		return "PledgeSubjectRealty{" +
				"area=" + area +
				", cadastralNum='" + cadastralNum + '\'' +
				", conditionalNum='" + conditionalNum + '\'' +
				'}';
	}
}
