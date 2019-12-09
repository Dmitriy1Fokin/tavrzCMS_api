package ru.fds.tavrzcms3.domain;

import lombok.*;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Data
@NoArgsConstructor
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
	private List<PledgeSubjectBuilding> pledgeSubjectBuildings;

	@OneToMany(mappedBy = "marketSegmentRoom")
	private List<PledgeSubjectRoom> pledgeSubjectRooms;

	@OneToMany(mappedBy = "marketSegmentBuilding")
	private List<PledgeSubjectRoom> pledgeSubjectRoomBuilding;

	@Override
	public String toString() {
		return "MarketSegment{" +
				"marketSegmentId=" + marketSegmentId +
				", name='" + name + '\'' +
				'}';
	}
}
