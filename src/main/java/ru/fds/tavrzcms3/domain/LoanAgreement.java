package ru.fds.tavrzcms3.domain;

import lombok.*;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

	@Singular
	@ManyToMany
	@JoinTable(name = "kd_dz", joinColumns = @JoinColumn(name ="kd_id"), inverseJoinColumns = @JoinColumn(name ="dz_id"))
	private List<PledgeAgreement> pledgeAgreements;

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
