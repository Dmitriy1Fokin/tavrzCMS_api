package ru.fds.tavrzcms3.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "market_segment")
public class MarketSegment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="market_segment_id")
	private int marketSegmentId;
	
	@Column(name = "name")
	private String name;
	
	@OneToMany(mappedBy = "marketSegment")
	private List<PledgeSubjectRealtyBuilding> pledgeSubjectRealtyBuildings;
	
	@OneToMany(mappedBy = "marketSegmentRoom")
	private List<PledgeSubjectRealtyRoom> pledgeSubjectRealtyRooms;
	
	@OneToMany(mappedBy = "marketSegmentBuilding")
	private List<PledgeSubjectRealtyRoom> pledgeSubjectRealtyRoomBuilding;

	public int getMarketSegmentId() {
		return marketSegmentId;
	}

	public void setMarketSegmentId(int marketSegmentId) {
		this.marketSegmentId = marketSegmentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PledgeSubjectRealtyBuilding> getPledgeSubjectRealtyBuildings() {
		return pledgeSubjectRealtyBuildings;
	}

	public void setPledgeSubjectRealtyBuildings(List<PledgeSubjectRealtyBuilding> pledgeSubjectRealtyBuildings) {
		this.pledgeSubjectRealtyBuildings = pledgeSubjectRealtyBuildings;
	}

	public List<PledgeSubjectRealtyRoom> getPledgeSubjectRealtyRooms() {
		return pledgeSubjectRealtyRooms;
	}

	public void setPledgeSubjectRealtyRooms(List<PledgeSubjectRealtyRoom> pledgeSubjectRealtyRooms) {
		this.pledgeSubjectRealtyRooms = pledgeSubjectRealtyRooms;
	}

	public List<PledgeSubjectRealtyRoom> getPledgeSubjectRealtyRoomBuilding() {
		return pledgeSubjectRealtyRoomBuilding;
	}

	public void setPledgeSubjectRealtyRoomBuilding(List<PledgeSubjectRealtyRoom> pledgeSubjectRealtyRoomBuilding) {
		this.pledgeSubjectRealtyRoomBuilding = pledgeSubjectRealtyRoomBuilding;
	}

	@Override
	public String toString() {
		return "MarketSegment{" +
				"marketSegmentId=" + marketSegmentId +
				", name='" + name + '\'' +
				'}';
	}
}
