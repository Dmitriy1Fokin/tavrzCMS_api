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
@Table(name = "land_category")
public class LandCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="land_category_id")
	private int landCategoryid;
	
	@Column(name ="name")
	private String name;
	
	@OneToMany(mappedBy = "landCategory")
	private List<PledgeSubjectRealtyLandLease> pledgeSubjectRealtyLandLeases;
	
	@OneToMany(mappedBy = "landCategory")
	private List<PledgeSubjectRealtyLandOwnership> pledgeSubjectRealtyLandOwnerships;

	public int getLandCategoryid() {
		return landCategoryid;
	}

	public void setLandCategoryid(int landCategoryid) {
		this.landCategoryid = landCategoryid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PledgeSubjectRealtyLandLease> getPledgeSubjectRealtyLandLeases() {
		return pledgeSubjectRealtyLandLeases;
	}

	public void setPledgeSubjectRealtyLandLeases(List<PledgeSubjectRealtyLandLease> pledgeSubjectRealtyLandLeases) {
		this.pledgeSubjectRealtyLandLeases = pledgeSubjectRealtyLandLeases;
	}

	public List<PledgeSubjectRealtyLandOwnership> getPledgeSubjectRealtyLandOwnerships() {
		return pledgeSubjectRealtyLandOwnerships;
	}

	public void setPledgeSubjectRealtyLandOwnerships(List<PledgeSubjectRealtyLandOwnership> pledgeSubjectRealtyLandOwnerships) {
		this.pledgeSubjectRealtyLandOwnerships = pledgeSubjectRealtyLandOwnerships;
	}

	@Override
	public String toString() {
		return "LandCategory{" +
				"landCategoryid=" + landCategoryid +
				", name='" + name + '\'' +
				'}';
	}
}
