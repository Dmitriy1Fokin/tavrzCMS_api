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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "cost_history")
public class CostHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="cost_history_id")
	private long costHistoryId;

	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="date")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateConclusion;

	@NotNull(message = "Обязательно для заполнения")
	@PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="zs_dz")
	private double zsDz;

	@NotNull(message = "Обязательно для заполнения")
	@PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="zs_zz")
	private double zsZz;

	@NotNull(message = "Обязательно для заполнения")
	@PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="rs_dz")
	private double rsDz;

	@NotNull(message = "Обязательно для заполнения")
	@PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="rs_zz")
	private double rsZz;

	@NotNull(message = "Обязательно для заполнения")
	@PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="ss")
	private double ss;

	@Column(name ="appraiser")
	private String appraiser;

	@Column(name ="num_appraisal_report")
	private String appraisalReportNum;

	@Column(name ="date_appraisal_report")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date appraisalReportDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pledge_subject_id")
	private PledgeSubject pledgeSubject;

	public long getCostHistoryId() {
		return costHistoryId;
	}

	public void setCostHistoryId(long costHistoryId) {
		this.costHistoryId = costHistoryId;
	}

	public Date getDateConclusion() {
		return dateConclusion;
	}

	public void setDateConclusion(Date dateConclusion) {
		this.dateConclusion = dateConclusion;
	}

	public double getZsDz() {
		return zsDz;
	}

	public void setZsDz(double zsDz) {
		this.zsDz = zsDz;
	}

	public double getZsZz() {
		return zsZz;
	}

	public void setZsZz(double zsZz) {
		this.zsZz = zsZz;
	}

	public double getRsDz() {
		return rsDz;
	}

	public void setRsDz(double rsDz) {
		this.rsDz = rsDz;
	}

	public double getRsZz() {
		return rsZz;
	}

	public void setRsZz(double rsZz) {
		this.rsZz = rsZz;
	}

	public double getSs() {
		return ss;
	}

	public void setSs(double ss) {
		this.ss = ss;
	}

	public String getAppraiser() {
		return appraiser;
	}

	public void setAppraiser(String appraiser) {
		this.appraiser = appraiser;
	}

	public String getAppraisalReportNum() {
		return appraisalReportNum;
	}

	public void setAppraisalReportNum(String appraisalReportNum) {
		this.appraisalReportNum = appraisalReportNum;
	}

	public Date getAppraisalReportDate() {
		return appraisalReportDate;
	}

	public void setAppraisalReportDate(Date appraisalReportDate) {
		this.appraisalReportDate = appraisalReportDate;
	}

	public PledgeSubject getPledgeSubject() {
		return pledgeSubject;
	}

	public void setPledgeSubject(PledgeSubject pledgeSubject) {
		this.pledgeSubject = pledgeSubject;
	}

	@Override
	public String toString() {
		return "CostHistory{" +
				"costHistoryId=" + costHistoryId +
				", dateConclusion=" + dateConclusion +
				", zsDz=" + zsDz +
				", zsZz=" + zsZz +
				", rsDz=" + rsDz +
				", rsZz=" + rsZz +
				", ss=" + ss +
				", appraiser='" + appraiser + '\'' +
				", appraisalReportNum='" + appraisalReportNum + '\'' +
				", appraisalReportDate=" + appraisalReportDate +
				", pledgeSubject=" + pledgeSubject +
				'}';
	}
}
