package ru.fds.tavrzcms3.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.StatusOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfMonitoring;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "monitoring")
public class Monitoring {
	
	@Id
	@SequenceGenerator(name = "monitoring_sequence", sequenceName = "monitoring_monitoring_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "monitoring_sequence")
	@Column(name ="monitoring_id")
	private Long monitoringId;

	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="date", columnDefinition = "DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateMonitoring;

	@NotNull(message = "Обязательно для заполнения")
	@Convert(converter = StatusOfMonitoring.Converter.class)
	@Column(name ="status")
	private StatusOfMonitoring statusMonitoring;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="employee")
	private String employee;

	@NotNull(message = "Обязательно для заполнения")
	@Convert(converter = TypeOfMonitoring.Converter.class)
	@Column(name ="type")
	private TypeOfMonitoring typeOfMonitoring;

	@Column(name = "notice")
	private String notice;

	@Column(name = "collateral_value")
	private BigDecimal collateralValue;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pledge_subject_id")
	private PledgeSubject pledgeSubject;

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
