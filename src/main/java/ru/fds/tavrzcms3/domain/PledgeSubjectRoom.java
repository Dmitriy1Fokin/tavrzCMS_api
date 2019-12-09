package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@SuperBuilder
@Table(name = "pledge_realty_room")
@SecondaryTable(name = "pledge_realty_prime", pkJoinColumns = @PrimaryKeyJoinColumn(name = "pledge_subject_id"))
public class PledgeSubjectRoom extends PledgeSubject {

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

    @NotBlank(message = "Обязательно для заполнения")
	@Column(name ="floor_location")
	private String floorLocation;

    @Valid
    @OneToOne(mappedBy = "pledgeSubjectRoom")
    @JsonIgnore
    private PledgeSubject pledgeSubject;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "market_segment_id")
	@JsonIgnore
	private MarketSegment marketSegmentRoom;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "market_segment_id_building")
	@JsonIgnore
	private MarketSegment marketSegmentBuilding;

	public PledgeSubjectRoom(){
		super.setTypeOfCollateral(TypeOfCollateral.PREMISE);
	}

	@Override
	public String toString() {
		return "PledgeSubjectRoom{" +
				"floorLocation='" + floorLocation + '\'' +
				'}';
	}
}
