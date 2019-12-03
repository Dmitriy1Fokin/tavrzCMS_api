package ru.fds.tavrzcms3.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cost_history")
public class CostHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="cost_history_id")
	private Long costHistoryId;

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
				'}';
	}
}
