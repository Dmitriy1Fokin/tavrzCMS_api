package ru.fds.tavrzcms3.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "insurance")
public class Insurance {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="insurance_id")
	private Long insuranceId;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="num_insurance")
	private String numInsurance;

	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="date_begin", columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateBeginInsurance;

	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="date_end", columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateEndInsurance;

	@NotNull(message = "Обязательно для заполнения")
	@PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="sum_insured")
	private double sumInsured;

	@NotNull(message = "Обязательно для заполнения")
    @Column(name ="date_insurance_contract", columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateInsuranceContract;

    @Pattern(regexp = "да|нет", message = "Возможные варианты: да, нет")
	@Column(name ="payment_of_insurance_premium")
	private String paymentOfInsurancePremium;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name = "franchise_amount")
    private Double franchiseAmount;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pledgesubject_id")
	private PledgeSubject pledgeSubject;

    @Override
    public String toString() {
        return "Insurance{" +
                "insuranceId=" + insuranceId +
                ", numInsurance='" + numInsurance + '\'' +
                ", dateBeginInsurance=" + dateBeginInsurance +
                ", dateEndInsurance=" + dateEndInsurance +
                ", sumInsured=" + sumInsured +
                ", dateInsuranceContract=" + dateInsuranceContract +
                ", paymentOfInsurancePremium='" + paymentOfInsurancePremium + '\'' +
                '}';
    }
}
