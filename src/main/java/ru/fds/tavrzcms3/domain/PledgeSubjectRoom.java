package ru.fds.tavrzcms3.domain;

import lombok.*;
import ru.fds.tavrzcms3.dictionary.MarketSegment;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class PledgeSubjectRoom{

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    @Column(name ="area_room", table = "pledge_realty_room")
    private double area;

    @Pattern(regexp = "[0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+",
            message = "Неверное значение")
    @Column(name ="cadastral_num_room", table = "pledge_realty_room")
    private String cadastralNum;

    @Column(name ="conditional_num_room", table = "pledge_realty_room")
    private String conditionalNum;

    @NotBlank(message = "Обязательно для заполнения")
	@Column(name ="floor_location", table = "pledge_realty_room")
	private String floorLocation;

	@Convert(converter = MarketSegment.Converter.class)
    @Column(name ="market_segment_room", table = "pledge_realty_room")
	private MarketSegment marketSegmentRoom;

	@Convert(converter = MarketSegment.Converter.class)
    @Column(name ="market_segment_building", table = "pledge_realty_room")
	private MarketSegment marketSegmentBuilding;
}
