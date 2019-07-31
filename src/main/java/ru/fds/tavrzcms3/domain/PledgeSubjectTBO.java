package ru.fds.tavrzcms3.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pledge_tbo")
public class PledgeSubjectTBO extends PledgeSubject {
	
	@Column(name ="count_of_tbo")
	private int countOfTBO;
	
	@Column(name ="carrying_amount")
	private double carryingAmount;
	
	@Column(name ="type_of_tbo")
	private String typeOfTBO;
	
	@OneToOne(mappedBy = "pledgeSubjectTBO")
	private PledgeSubject pledgeSubject;

	public int getCountOfTBO() {
		return countOfTBO;
	}

	public void setCountOfTBO(int countOfTBO) {
		this.countOfTBO = countOfTBO;
	}

	public double getCarryingAmount() {
		return carryingAmount;
	}

	public void setCarryingAmount(double carryingAmount) {
		this.carryingAmount = carryingAmount;
	}

	public String getTypeOfTBO() {
		return typeOfTBO;
	}

	public void setTypeOfTBO(String typeOfTBO) {
		this.typeOfTBO = typeOfTBO;
	}

	public PledgeSubject getPledgeSubject() {
		return pledgeSubject;
	}

	public void setPledgeSubject(PledgeSubject pledgeSubject) {
		this.pledgeSubject = pledgeSubject;
	}

    @Override
    public String toString() {
        return "PledgeSubjectTBO{" +
                "countOfTBO=" + countOfTBO +
                ", carryingAmount=" + carryingAmount +
                ", typeOfTBO='" + typeOfTBO + '\'' +
//                ", pledgeSubject=" + pledgeSubject +
                '}';
    }
}
