package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@AllArgsConstructor
@SuperBuilder
@Table(name = "pledge_realty_room")
public class PledgeSubjectRealtyRoom extends PledgeSubjectRealty {

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="floor_location")
	private String floorLocation;

	@Valid
	@OneToOne(mappedBy = "pledgeSubjectRealtyRoom")
	@JsonIgnore
	private PledgeSubjectRealty pledgeSubjectRealty;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "market_segment_id")
	@JsonIgnore
	private MarketSegment marketSegmentRoom;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "market_segment_id_building")
	@JsonIgnore
	private MarketSegment marketSegmentBuilding;

	public PledgeSubjectRealtyRoom(){
		super.setTypeOfCollateral("Недвижимость - помещение");
	}

	@Override
	public String toString() {
		return "PledgeSubjectRealtyRoom{" +
				"floorLocation='" + floorLocation + '\'' +
				'}';
	}
}
