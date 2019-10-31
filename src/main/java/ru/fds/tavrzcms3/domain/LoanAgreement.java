package ru.fds.tavrzcms3.domain;

import org.springframework.format.annotation.DateTimeFormat;

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
import javax.validation.constraints.*;

@Entity
@Table(name = "kd")
public class LoanAgreement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="kd_id")
	private long loanAgreementId;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="num_kd")
	private String numLA;

	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="date_begin_kd")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateBeginLA;

	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="date_end_kd")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateEndLA;

	@Pattern(regexp = "открыт|закрыт",
			message = "Возможные варианты: открыт, закрыт")
	@Column(name ="status")
	private String statusLA;

	@NotNull(message = "Обязательно для заполнения")
	@PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="loan_amount")
	private double amountLA;

	@NotNull(message = "Обязательно для заполнения")
	@PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="loan_debt")
	private double debtLA;

	@NotNull(message = "Обязательно для заполнения")
	@Positive(message = "Значение должно быть больше нуля")
	@Max(value = 1, message = "Значение не может быть больше 1")
	@Column(name ="interest_rate")
	private double interestRateLA;

	@NotNull(message = "Обязательно для заполнения")
	@Min(value = 1, message = "Значение должно быть от 1 до 5")
	@Max(value = 5, message = "Значение должно быть от 1 до 5")
	@Column(name ="pfo")
	private byte pfo;

	@NotNull(message = "Обязательно для заполнения")
	@Min(value = 1, message = "Значение должно быть от 1 до 5")
	@Max(value = 5, message = "Значение должно быть от 1 до 5")
	@Column(name ="quality_category")
	private byte qualityCategory;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loaner_id")
	private Client client;
	
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
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
				'}';
	}
}
