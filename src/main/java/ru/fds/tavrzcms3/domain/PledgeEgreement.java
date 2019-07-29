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
@Table(name = "dz")
public class PledgeEgreement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="dz_id")
	private long pledgeEgreementId;
	
	@Column(name ="num_dz")
	private String numPE;
	
	@Column(name ="date_begin_dz")
	@Temporal(TemporalType.DATE)
	private Date dateBeginPE;
	
	@Column(name ="date_end_dz")
	@Temporal(TemporalType.DATE)
	private Date dateEndPE;
	
	@Column(name ="perv_posl")
	private String pervPosl;
	
	@Column(name ="status")
	private String statusPE;
	
	@Column(name ="notice")
	private String noticePE;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "kd_dz", joinColumns = @JoinColumn(name ="dz_id"), inverseJoinColumns = @JoinColumn(name ="kd_id"))
	private List<LoanEgreement> loanEgreements;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pledgor_id")
	private Client pledgor;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "dz_ps", joinColumns = @JoinColumn(name ="dz_id"), inverseJoinColumns = @JoinColumn(name ="pledge_subject_id"))
	private List<PledgeSubject> pledgeSubjects;

	public long getPledgeEgreementId() {
		return pledgeEgreementId;
	}

	public void setPledgeEgreementId(long pledgeEgreementId) {
		this.pledgeEgreementId = pledgeEgreementId;
	}

	public String getNumPE() {
		return numPE;
	}

	public void setNumPE(String numPE) {
		this.numPE = numPE;
	}

	public Date getDateBeginPE() {
		return dateBeginPE;
	}

	public void setDateBeginPE(Date dateBeginPE) {
		this.dateBeginPE = dateBeginPE;
	}

	public Date getDateEndPE() {
		return dateEndPE;
	}

	public void setDateEndPE(Date dateEndPE) {
		this.dateEndPE = dateEndPE;
	}

	public String getPervPosl() {
		return pervPosl;
	}

	public void setPervPosl(String pervPosl) {
		this.pervPosl = pervPosl;
	}

	public String getStatusPE() {
		return statusPE;
	}

	public void setStatusPE(String statusPE) {
		this.statusPE = statusPE;
	}

	public String getNoticePE() {
		return noticePE;
	}

	public void setNoticePE(String noticePE) {
		this.noticePE = noticePE;
	}

	public List<LoanEgreement> getLoanEgreements() {
		return loanEgreements;
	}

	public void setLoanEgreements(List<LoanEgreement> loanEgreements) {
		this.loanEgreements = loanEgreements;
	}

	public Client getPledgor() {
		return pledgor;
	}

	public void setPledgor(Client pledgor) {
		this.pledgor = pledgor;
	}

	public List<PledgeSubject> getPledgeSubjects() {
		return pledgeSubjects;
	}

	public void setPledgeSubjects(List<PledgeSubject> pledgeSubjects) {
		this.pledgeSubjects = pledgeSubjects;
	}

	@Override
	public String toString() {
		return "PledgeEgreement{" +
				"pledgeEgreementId=" + pledgeEgreementId +
				", numPE='" + numPE + '\'' +
				", dateBeginPE=" + dateBeginPE +
				", dateEndPE=" + dateEndPE +
				", pervPosl='" + pervPosl + '\'' +
				", statusPE='" + statusPE + '\'' +
				", noticePE='" + noticePE + '\'' +
//				", pledgor=" + pledgor +
				'}';
	}
}
