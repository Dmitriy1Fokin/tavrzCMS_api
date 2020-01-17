package ru.fds.tavrzcms3.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;
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
