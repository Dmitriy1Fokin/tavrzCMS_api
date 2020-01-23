package ru.fds.tavrzcms3.domain.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fds.tavrzcms3.dictionary.MarketSegment;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class PledgeSubjectBuilding{

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    @Column(name ="area_building", table = "pledge_realty_building")
    private Double area;

	@NotNull(message = "Обязательно для заполнения")
    @Pattern(regexp = "[0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+",
            message = "Неверное значение")
    @Column(name ="cadastral_num_building", table = "pledge_realty_building")
    private String cadastralNum;

    @Column(name ="conditional_num_building", table = "pledge_realty_building")
    private String conditionalNum;

    @NotNull(message = "Обязательно для заполнения")
	@Min(value = 1, message = "Неверное значение")
	@Max(value = 100, message = "Неверное значение")
	@Column(name ="readiness_degree", table = "pledge_realty_building")
	private Integer readinessDegree;

	@NotNull(message = "Обязательно для заполнения")
	@Min(value = 1800, message = "Неверное значение")
	@Max(value = 2100, message = "Неверное значение")
	@Column(name ="year_of_construction", table = "pledge_realty_building")
	private Integer yearOfConstruction;

	@NotNull
	@Convert(converter = MarketSegment.Converter.class)
    @Column(name = "market_segment", table = "pledge_realty_building")
	private MarketSegment marketSegment;
}
