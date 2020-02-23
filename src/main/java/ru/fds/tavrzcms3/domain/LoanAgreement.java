package ru.fds.tavrzcms3.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;

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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "kd")
public class LoanAgreement {
	
	@Id
	@SequenceGenerator(name = "kd_sequence", sequenceName = "kd_kd_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kd_sequence")
	@Column(name ="kd_id")
	private Long loanAgreementId;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="num_kd")
	private String numLA;

	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="date_begin_kd", columnDefinition = "DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateBeginLA;

	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="date_end_kd", columnDefinition = "DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateEndLA;

	@NotNull
	@Convert(converter = StatusOfAgreement.Converter.class)
	@Column(name ="status")
	private StatusOfAgreement statusLA;

	@NotNull(message = "Обязательно для заполнения")
	@PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="loan_amount")
	private BigDecimal amountLA;

	@NotNull(message = "Обязательно для заполнения")
	@PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="loan_debt")
	private BigDecimal debtLA;

	@NotNull(message = "Обязательно для заполнения")
	@Positive(message = "Значение должно быть больше нуля")
	@Max(value = 1, message = "Значение не может быть больше 1")
	@Column(name ="interest_rate")
	private Double interestRateLA;

	@NotNull(message = "Обязательно для заполнения")
	@Min(value = 1, message = "Значение должно быть от 1 до 5")
	@Max(value = 5, message = "Значение должно быть от 1 до 5")
	@Column(name ="pfo")
	private Byte pfo;

	@NotNull(message = "Обязательно для заполнения")
	@Min(value = 1, message = "Значение должно быть от 1 до 5")
	@Max(value = 5, message = "Значение должно быть от 1 до 5")
	@Column(name ="quality_category")
	private Byte qualityCategory;

	@NotNull(message = "Обязательно для заполнения")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loaner_id")
	private Client client;

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
