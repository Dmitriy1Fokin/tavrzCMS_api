package ru.fds.tavrzcms3.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "kd")
public class LoanAgreement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="kd_id")
	private long loanAgreementId;
	
	@Column(name ="num_kd")
	private String numLA;
	
	@Column(name ="date_begin_kd")
	@Temporal(TemporalType.DATE)
	private Date dateBeginLA;

	@Column(name ="date_end_kd")
	@Temporal(TemporalType.DATE)
	private Date dateEndLA;
	
	@Column(name ="status")
	private String statusLA;
	
	@Column(name ="loan_amount")
	private double amountLA;
	
	@Column(name ="loan_debt")
	private double debtLA;
	
	@Column(name ="interest_rate")
	private double interestRateLA;
	
	@Column(name ="pfo")
	private byte pfo;
	
	@Column(name ="quality_category")
	private byte qualityCategory;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loaner_id")
	private Client loaner;
	
	@ManyToMany
	@JoinTable(name = "kd_dz", joinColumns = @JoinColumn(name ="kd_id"), inverseJoinColumns = @JoinColumn(name ="dz_id"))
	private List<PledgeAgreement> pledgeAgreements;

	public long getLoanAgreementId() {
		return loanAgreementId;
	}

	public void setLoanAgreementId(long loanAgreementId) {
		this.loanAgreementId = loanAgreementId;
	}

	public String getNumLA() {
		return numLA;
	}

	public void setNumLA(String numLA) {
		this.numLA = numLA;
	}

	public Date getDateBeginLA() {
		return dateBeginLA;
	}

	public void setDateBeginLA(Date dateBeginLA) {
		this.dateBeginLA = dateBeginLA;
	}

	public Date getDateEndLA() {
		return dateEndLA;
	}

	public void setDateEndLA(Date dateEndLA) {
		this.dateEndLA = dateEndLA;
	}

	public String getStatusLA() {
		return statusLA;
	}

	public void setStatusLA(String statusLA) {
		this.statusLA = statusLA;
	}

	public double getAmountLA() {
		return amountLA;
	}

	public void setAmountLA(double amountLA) {
		this.amountLA = amountLA;
	}

	public double getDebtLA() {
		return debtLA;
	}

	public void setDebtLA(double debtLA) {
		this.debtLA = debtLA;
	}

	public double getInterestRateLA() {
		return interestRateLA;
	}

	public void setInterestRateLA(double interestRateLA) {
		this.interestRateLA = interestRateLA;
	}

	public byte getPfo() {
		return pfo;
	}

	public void setPfo(byte pfo) {
		this.pfo = pfo;
	}

	public byte getQualityCategory() {
		return qualityCategory;
	}

	public void setQualityCategory(byte qualityCategory) {
		this.qualityCategory = qualityCategory;
	}

	public Client getLoaner() {
		return loaner;
	}

	public void setLoaner(Client loaner) {
		this.loaner = loaner;
	}

	public List<PledgeAgreement> getPledgeAgreements() {
		return pledgeAgreements;
	}

	public void setPledgeAgreements(List<PledgeAgreement> pledgeAgreements) {
		this.pledgeAgreements = pledgeAgreements;
	}

	@Override
	public String toString() {
		return "LoanAgreement{" +
				"loanAgreementId=" + loanAgreementId +
				", numLA='" + numLA + '\'' +
				", dateBeginLA=" + dateBeginLA +
				", dateEndLA=" + dateEndLA +
				", statusLA='" + statusLA + '\'' +
				", amountLA=" + amountLA +
				", debtLA=" + debtLA +
				", interestRateLA=" + interestRateLA +
				", pfo=" + pfo +
				", qualityCategory=" + qualityCategory +
//				", loaner=" + loaner +
				'}';
	}
}
