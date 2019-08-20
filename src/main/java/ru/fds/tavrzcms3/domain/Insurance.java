package ru.fds.tavrzcms3.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "insurance")
public class Insurance {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="insurance_id")
	private long insuranceId;
	
	@Column(name ="num_insurance")
	private String numInsurance;
	
	@Column(name ="date_begin")
	@Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateBeginInsurance;
	
	@Column(name ="date_end")
	@Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateEndInsurance;

	@Column(name ="sum_insured")
	private double sumInsured;

    @Column(name ="date_insurance_contract")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateInsuranceContract;
	
	@Column(name ="payment_of_insurance_premium")
	private String paymentOfInsurancePremium;

	@Column(name = "franchise_amount")
    private Double franchiseAmount;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pledgesubject_id")
	private PledgeSubject pledgeSubject;

    public long getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(long insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getNumInsurance() {
        return numInsurance;
    }

    public void setNumInsurance(String numInsurance) {
        this.numInsurance = numInsurance;
    }

    public Date getDateBeginInsurance() {
        return dateBeginInsurance;
    }

    public void setDateBeginInsurance(Date dateBeginInsurance) {
        this.dateBeginInsurance = dateBeginInsurance;
    }

    public Date getDateEndInsurance() {
        return dateEndInsurance;
    }

    public void setDateEndInsurance(Date dateEndInsurance) {
        this.dateEndInsurance = dateEndInsurance;
    }

    public double getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(double sumInsured) {
        this.sumInsured = sumInsured;
    }

    public Date getDateInsuranceContract() {
        return dateInsuranceContract;
    }

    public void setDateInsuranceContract(Date dateInsuranceContract) {
        this.dateInsuranceContract = dateInsuranceContract;
    }

    public String getPaymentOfInsurancePremium() {
        return paymentOfInsurancePremium;
    }

    public void setPaymentOfInsurancePremium(String paymentOfInsurancePremium) {
        this.paymentOfInsurancePremium = paymentOfInsurancePremium;
    }

    public Double getFranchiseAmount() {
        return franchiseAmount;
    }

    public void setFranchiseAmount(Double franchiseAmount) {
        this.franchiseAmount = franchiseAmount;
    }

    public PledgeSubject getPledgeSubject() {
        return pledgeSubject;
    }

    public void setPledgeSubject(PledgeSubject pledgeSubject) {
        this.pledgeSubject = pledgeSubject;
    }

    @Override
    public String toString() {
        return "Insurance{" +
                "insuranceId=" + insuranceId +
                ", numInsurance='" + numInsurance + '\'' +
                ", dateBeginInsurance=" + dateBeginInsurance +
                ", dateEndInsurance=" + dateEndInsurance +
                ", sumInsured=" + sumInsured +
                ", dateInsuranceContract=" + dateInsuranceContract +
                ", paymentOfInsurancePremium='" + paymentOfInsurancePremium + '\'' +
//                ", pledgeSubject=" + pledgeSubject +
                '}';
    }
}
