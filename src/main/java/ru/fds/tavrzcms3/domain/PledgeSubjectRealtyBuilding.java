package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pledge_realty_building")
public class PledgeSubjectRealtyBuilding extends PledgeSubjectRealty {
	
	@Column(name ="readiness_degree")
	private int readinessDegree;
	
	@Column(name ="year_of_construction")
	private int yearOfConstruction;
	
	@OneToOne(mappedBy = "pledgeSubjectRealtyBuilding")
	@JsonIgnore
	private PledgeSubjectRealty pledgeSubjectRealty;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "market_segment_id")
	@JsonIgnore
	private MarketSegment marketSegment;

	public PledgeSubjectRealtyBuilding(){
		super.setTypeOfCollateral("Недвижимость - здание/сооружение");
	}

	public int getReadinessDegree() {
		return readinessDegree;
	}

	public void setReadinessDegree(int readinessDegree) {
		this.readinessDegree = readinessDegree;
	}

	public int getYearOfConstruction() {
		return yearOfConstruction;
	}

	public void setYearOfConstruction(int yearOfConstruction) {
		this.yearOfConstruction = yearOfConstruction;
	}

	public PledgeSubjectRealty getPledgeSubjectRealty() {
		return pledgeSubjectRealty;
	}

	public void setPledgeSubjectRealty(PledgeSubjectRealty pledgeSubjectRealty) {
		this.pledgeSubjectRealty = pledgeSubjectRealty;
	}

	public MarketSegment getMarketSegment() {
		return marketSegment;
	}

	public void setMarketSegment(MarketSegment marketSegment) {
		this.marketSegment = marketSegment;
	}

	@Override
	public String toString() {
		return "PledgeSubjectRealtyBuilding{" +
				"readinessDegree=" + readinessDegree +
				", yearOfConstruction=" + yearOfConstruction +
				'}';
	}
}
