package ru.fds.tavrzcms3.domain.embedded;

import lombok.*;
import ru.fds.tavrzcms3.dictionary.MarketSegment;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class PledgeSubjectBuilding{

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    @Column(name ="area_building", table = "pledge_realty_building")
    private double area;

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
	private int readinessDegree;

	@NotNull(message = "Обязательно для заполнения")
	@Min(value = 1800, message = "Неверное значение")
	@Max(value = 2100, message = "Неверное значение")
	@Column(name ="year_of_construction", table = "pledge_realty_building")
	private int yearOfConstruction;

	@NotNull
	@Convert(converter = MarketSegment.Converter.class)
    @Column(name = "market_segment", table = "pledge_realty_building")
	private MarketSegment marketSegment;
}
