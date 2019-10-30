package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "pledge_realty_land_ownership")
public class PledgeSubjectRealtyLandOwnership extends PledgeSubjectRealty {

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="permitted_use")
	private String permittedUse;

	@Pattern(regexp = "да|нет", message = "Возможные варианты: да, нет")
	@Column(name ="built_up")
	private String builtUp;

	@Pattern(regexp = "^$|[0-9]{2}:[0-9]{3,7}:[0-9]+",
			message = "Неверное значение")
	@Column(name ="cadastral_num_of_building")
	private String cadastralNumOfBuilding;

	@Valid
	@OneToOne(mappedBy = "pledgeSubjectRealtyLandOwnership")
	@JsonIgnore
	private PledgeSubjectRealty pledgeSubjectRealty;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "land_category_id")
	@JsonIgnore
	private LandCategory landCategory;

	public PledgeSubjectRealtyLandOwnership(){
		super.setTypeOfCollateral("Недвижимость - ЗУ - собственность");
	}

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
				'}';
	}
}
