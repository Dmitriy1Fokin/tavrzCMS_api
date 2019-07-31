package ru.fds.tavrzcms3.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pledge_realty_land_ownership")
public class PledgeSubjectRealtyLandOwnership extends PledgeSubjectRealty {

	@Column(name ="permitted_use")
	private String permittedUse;
	
	@Column(name ="built_up")
	private String builtUp;
	
	@Column(name ="cadastral_num_of_building")
	private String cadastralNumOfBuilding;
	
	@OneToOne(mappedBy = "pledgeSubjectRealtyLandOwnership")
	private PledgeSubjectRealty pledgeSubjectRealty;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "land_category_id")
	private LandCategory landCategory;

	public String getPermittedUse() {
		return permittedUse;
	}

	public void setPermittedUse(String permittedUse) {
		this.permittedUse = permittedUse;
	}

	public String getBuiltUp() {
		return builtUp;
	}

	public void setBuiltUp(String builtUp) {
		this.builtUp = builtUp;
	}

	public String getCadastralNumOfBuilding() {
		return cadastralNumOfBuilding;
	}

	public void setCadastralNumOfBuilding(String cadastralNumOfBuilding) {
		this.cadastralNumOfBuilding = cadastralNumOfBuilding;
	}

	public PledgeSubjectRealty getPledgeSubjectRealty() {
		return pledgeSubjectRealty;
	}

	public void setPledgeSubjectRealty(PledgeSubjectRealty pledgeSubjectRealty) {
		this.pledgeSubjectRealty = pledgeSubjectRealty;
	}

	public LandCategory getLandCategory() {
		return landCategory;
	}

	public void setLandCategory(LandCategory landCategory) {
		this.landCategory = landCategory;
	}

	@Override
	public String toString() {
		return "PledgeSubjectRealtyLandOwnership{" +
				"permittedUse='" + permittedUse + '\'' +
				", builtUp='" + builtUp + '\'' +
				", cadastralNumOfBuilding='" + cadastralNumOfBuilding + '\'' +
//				", pledgeSubjectRealty=" + pledgeSubjectRealty +
//				", landCategory=" + landCategory +
				'}';
	}
}
