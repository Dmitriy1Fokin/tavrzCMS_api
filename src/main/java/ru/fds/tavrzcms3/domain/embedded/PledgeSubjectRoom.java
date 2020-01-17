package ru.fds.tavrzcms3.domain.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fds.tavrzcms3.dictionary.MarketSegment;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
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

    @NotNull(message = "Обязательно для заполнения")
    @Pattern(regexp = "[0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+",
            message = "Неверное значение")
    @Column(name ="cadastral_num_room", table = "pledge_realty_room")
    private String cadastralNum;

    @Column(name ="conditional_num_room", table = "pledge_realty_room")
    private String conditionalNum;

    @NotBlank(message = "Обязательно для заполнения")
	@Column(name ="floor_location", table = "pledge_realty_room")
	private String floorLocation;

    @NotNull
	@Convert(converter = MarketSegment.Converter.class)
    @Column(name ="market_segment_room", table = "pledge_realty_room")
	private MarketSegment marketSegmentRoom;

    @NotNull
	@Convert(converter = MarketSegment.Converter.class)
    @Column(name ="market_segment_building", table = "pledge_realty_room")
	private MarketSegment marketSegmentBuilding;
}
