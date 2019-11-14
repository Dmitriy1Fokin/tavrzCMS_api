package ru.fds.tavrzcms3.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "monitoring")
public class Monitoring {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="monitoring_id")
	private long monitoringId;

	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="date")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateMonitoring;

	@Pattern(regexp = "в наличии|утрата|частичная утрата", message = "Возможные варианты: в наличии, утрата, частичная утрата")
	@Column(name ="status")
	private String statusMonitoring;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="employee")
	private String employee;

	@Pattern(regexp = "документарный|визуальный", message = "Возможные варианты: документарный, визуальный")
	@Column(name ="type")
	private String typeOfMonitoring;

	@Column(name = "notice")
	private String notice;

	@Column(name = "collateral_value")
	private Double collateralValue;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pledge_subject_id")
	private PledgeSubject pledgeSubject;

	public Monitoring(){

	}

	public Monitoring(Monitoring monitoring){
		this.monitoringId = monitoring.monitoringId;
		this.dateMonitoring = monitoring.dateMonitoring;
		this.statusMonitoring = monitoring.statusMonitoring;
		this.employee = monitoring.employee;
		this.typeOfMonitoring = monitoring.typeOfMonitoring;
		this.notice = monitoring.notice;
		this.collateralValue = monitoring.collateralValue;
		this.pledgeSubject = monitoring.pledgeSubject;
	}

	@Override
	public String toString() {
		return "Monitoring{" +
				"monitoringId=" + monitoringId +
				", dateMonitoring=" + dateMonitoring +
				", statusMonitoring='" + statusMonitoring + '\'' +
				", employee='" + employee + '\'' +
				", typeOfMonitoring='" + typeOfMonitoring + '\'' +
				", notice='" + notice + '\'' +
				", collateralValue=" + collateralValue +
				'}';
	}
}
