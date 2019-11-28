package ru.fds.tavrzcms3.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.StatusOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfMonitoring;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
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

	@Convert(converter = StatusOfMonitoring.Converter.class)
	@Column(name ="status")
	private StatusOfMonitoring statusMonitoring;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="employee")
	private String employee;

	@Convert(converter = TypeOfMonitoring.Converter.class)
	@Column(name ="type")
	private TypeOfMonitoring typeOfMonitoring;

	@Column(name = "notice")
	private String notice;

	@Column(name = "collateral_value")
	private Double collateralValue;

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
