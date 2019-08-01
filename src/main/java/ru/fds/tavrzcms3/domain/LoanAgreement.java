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
	private String numLE;
	
	@Column(name ="date_begin_kd")
	@Temporal(TemporalType.DATE)
	private Date dateBeginLE;

	@Column(name ="date_end_kd")
	@Temporal(TemporalType.DATE)
	private Date dateEndLE;
	
	@Column(name ="status")
	private String statusLE;
	
	@Column(name ="loan_amount")
	private double amountLE;
	
	@Column(name ="loan_debt")
	private double debtLE;
	
	@Column(name ="interest_rate")
	private double interestRateLE;
	
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

	public String getNumLE() {
		return numLE;
	}

	public void setNumLE(String numLE) {
		this.numLE = numLE;
	}

	public Date getDateBeginLE() {
		return dateBeginLE;
	}

	public void setDateBeginLE(Date dateBeginLE) {
		this.dateBeginLE = dateBeginLE;
	}

	public Date getDateEndLE() {
		return dateEndLE;
	}

	public void setDateEndLE(Date dateEndLE) {
		this.dateEndLE = dateEndLE;
	}

	public String getStatusLE() {
		return statusLE;
	}

	public void setStatusLE(String statusLE) {
		this.statusLE = statusLE;
	}

	public double getAmountLE() {
		return amountLE;
	}

	public void setAmountLE(double amountLE) {
		this.amountLE = amountLE;
	}

	public double getDebtLE() {
		return debtLE;
	}

	public void setDebtLE(double debtLE) {
		this.debtLE = debtLE;
	}

	public double getInterestRateLE() {
		return interestRateLE;
	}

	public void setInterestRateLE(double interestRateLE) {
		this.interestRateLE = interestRateLE;
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
				", numLE='" + numLE + '\'' +
				", dateBeginLE=" + dateBeginLE +
				", dateEndLE=" + dateEndLE +
				", statusLE='" + statusLE + '\'' +
				", amountLE=" + amountLE +
				", debtLE=" + debtLE +
				", interestRateLE=" + interestRateLE +
				", pfo=" + pfo +
				", qualityCategory=" + qualityCategory +
				", loaner=" + loaner +
				'}';
	}
}
