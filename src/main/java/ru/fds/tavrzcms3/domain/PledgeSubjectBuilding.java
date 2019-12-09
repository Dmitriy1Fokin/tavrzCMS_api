package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "pledge_realty_building")
@SecondaryTable(name = "pledge_realty_prime", pkJoinColumns = @PrimaryKeyJoinColumn(name = "pledge_subject_id"))
public class PledgeSubjectBuilding extends PledgeSubject {

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    @Column(name ="area", table = "pledge_realty_prime")
    private double area;

    @Pattern(regexp = "[0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+",
            message = "Неверное значение")
    @Column(name ="cadastral_num", table = "pledge_realty_prime")
    private String cadastralNum;

    @Column(name ="conditional_num", table = "pledge_realty_prime")
    private String conditionalNum;

    @NotNull(message = "Обязательно для заполнения")
	@Min(value = 1, message = "Неверное значение")
	@Max(value = 100, message = "Неверное значение")
	@Column(name ="readiness_degree")
	private int readinessDegree;

	@NotNull(message = "Обязательно для заполнения")
	@Min(value = 1800, message = "Неверное значение")
	@Max(value = 2100, message = "Неверное значение")
	@Length(min = 4, max = 4, message = "Неверное значение")
	@Column(name ="year_of_construction")
	private int yearOfConstruction;

    @Valid
    @OneToOne(mappedBy = "pledgeSubjectBuilding")
    @JsonIgnore
    private PledgeSubject pledgeSubject;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "market_segment_id")
	@JsonIgnore
	private MarketSegment marketSegment;

	public PledgeSubjectBuilding(){
		super.setTypeOfCollateral(TypeOfCollateral.BUILDING);
	}

	@Override
	public String toString() {
		return "PledgeSubjectBuilding{" +
				"readinessDegree=" + readinessDegree +
				", yearOfConstruction=" + yearOfConstruction +
				'}';
	}
}
