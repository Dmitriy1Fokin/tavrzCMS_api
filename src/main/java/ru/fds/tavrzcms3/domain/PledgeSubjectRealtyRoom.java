package ru.fds.tavrzcms3.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pledge_realty_room")
public class PledgeSubjectRealtyRoom extends PledgeSubjectRealty {
	
	@Column(name ="floor_location")
	private String floorLocation;
	
	@OneToOne(mappedBy = "pledgeSubjectRealtyRoom")
	private PledgeSubjectRealty pledgeSubjectRealty;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "market_segment_id")
	private MarketSegment marketSegmentRoom;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "market_segment_id_building")
	private MarketSegment marketSegmentBuilding;

	public String getFloorLocation() {
		return floorLocation;
	}

	public void setFloorLocation(String floorLocation) {
		this.floorLocation = floorLocation;
	}

	public PledgeSubjectRealty getPledgeSubjectRealty() {
		return pledgeSubjectRealty;
	}

	public void setPledgeSubjectRealty(PledgeSubjectRealty pledgeSubjectRealty) {
		this.pledgeSubjectRealty = pledgeSubjectRealty;
	}

	public MarketSegment getMarketSegmentRoom() {
		return marketSegmentRoom;
	}

	public void setMarketSegmentRoom(MarketSegment marketSegmentRoom) {
		this.marketSegmentRoom = marketSegmentRoom;
	}

	public MarketSegment getMarketSegmentBuilding() {
		return marketSegmentBuilding;
	}

	public void setMarketSegmentBuilding(MarketSegment marketSegmentBuilding) {
		this.marketSegmentBuilding = marketSegmentBuilding;
	}

	@Override
	public String toString() {
		return "PledgeSubjectRealtyRoom{" +
				"floorLocation='" + floorLocation + '\'' +
				'}';
	}
}
