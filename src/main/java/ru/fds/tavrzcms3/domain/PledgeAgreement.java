package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Entity
@Table(name = "dz")
public class PledgeAgreement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="dz_id")
	private long pledgeAgreementId;
	
	@Column(name ="num_dz")
	private String numPA;
	
	@Column(name ="date_begin_dz")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateBeginPA;
	
	@Column(name ="date_end_dz")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateEndPA;
	
	@Column(name ="perv_posl")
	private String pervPosl;
	
	@Column(name ="status")
	private String statusPA;
	
	@Column(name ="notice")
	private String noticePA;

	@Column(name ="zs_dz")
	private double zsDz;

	@Column(name ="zs_zz")
	private double zsZz;

	@Column(name ="rs_dz")
	private double rsDz;

	@Column(name ="rs_zz")
	private double rsZz;

	@Column(name ="ss")
	private double ss;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "kd_dz", joinColumns = @JoinColumn(name ="dz_id"), inverseJoinColumns = @JoinColumn(name ="kd_id"))
	private List<LoanAgreement> loanAgreements;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pledgor_id")
	private Client pledgor;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "dz_ps", joinColumns = @JoinColumn(name ="dz_id"), inverseJoinColumns = @JoinColumn(name ="pledge_subject_id"))
	private List<PledgeSubject> pledgeSubjects;

	public long getPledgeAgreementId() {
		return pledgeAgreementId;
	}

	public void setPledgeAgreementId(long pledgeAgreementId) {
		this.pledgeAgreementId = pledgeAgreementId;
	}

	public String getNumPA() {
		return numPA;
	}

	public void setNumPA(String numPA) {
		this.numPA = numPA;
	}

	public Date getDateBeginPA() {
		return dateBeginPA;
	}

	public void setDateBeginPA(Date dateBeginPA) {
		this.dateBeginPA = dateBeginPA;
	}

	public Date getDateEndPA() {
		return dateEndPA;
	}

	public void setDateEndPA(Date dateEndPA) {
		this.dateEndPA = dateEndPA;
	}

	public String getPervPosl() {
		return pervPosl;
	}

	public void setPervPosl(String pervPosl) {
		this.pervPosl = pervPosl;
	}

	public String getStatusPA() {
		return statusPA;
	}

	public void setStatusPA(String statusPA) {
		this.statusPA = statusPA;
	}

	public String getNoticePA() {
		return noticePA;
	}

	public void setNoticePA(String noticePA) {
		this.noticePA = noticePA;
	}

	public double getZsDz() {
		return zsDz;
	}

	public double getZsZz() {
		return zsZz;
	}

	public double getRsDz() {
		return rsDz;
	}

	public double getRsZz() {
		return rsZz;
	}

	public double getSs() {
		return ss;
	}

	public List<LoanAgreement> getLoanAgreements() {
		return loanAgreements;
	}

	public void setLoanAgreements(List<LoanAgreement> loanAgreements) {
		this.loanAgreements = loanAgreements;
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
		return "PledgeAgreement{" +
				"pledgeAgreementId=" + pledgeAgreementId +
				", numPA='" + numPA + '\'' +
				", dateBeginPA=" + dateBeginPA +
				", dateEndPA=" + dateEndPA +
				", pervPosl='" + pervPosl + '\'' +
				", statusPA='" + statusPA + '\'' +
				", noticePA='" + noticePA + '\'' +
				", zsDz=" + zsDz +
				", zsZz=" + zsZz +
				", rsDz=" + rsDz +
				", rsZz=" + rsZz +
				", ss=" + ss +
				'}';
	}
}
