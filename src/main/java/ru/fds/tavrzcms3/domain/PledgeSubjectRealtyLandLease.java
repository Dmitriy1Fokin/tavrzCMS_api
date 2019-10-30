package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "pledge_realty_land_lease")
public class PledgeSubjectRealtyLandLease extends PledgeSubjectRealty {

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="permitted_use")
	private String permittedUse;

	@Pattern(regexp = "да|нет", message = "Возможные варианты: да, нет")
	@Column(name ="built_up")
	private String builtUp;

	@Pattern(regexp = "^$|([0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+(( *; *[0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+)|($))*)+",
			message = "Неверное значение. Если несколько кад№, указывать через \";\"")
	@Column(name ="cadastral_num_of_building")
	private String cadastralNumOfBuilding;

	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="begin_date_lease")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateBeginLease;

	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="end_date_lease")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateEndLease;

	@Valid
	@OneToOne(mappedBy = "pledgeSubjectRealtyLandLease")
	@JsonIgnore
	private PledgeSubjectRealty pledgeSubjectRealty;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "land_category_id")
	@JsonIgnore
	private LandCategory landCategory;

	public PledgeSubjectRealtyLandLease(){
		super.setTypeOfCollateral("Недвижимость - ЗУ - право аренды");
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

	public Date getDateBeginLease() {
		return dateBeginLease;
	}

	public void setDateBeginLease(Date dateBeginLease) {
		this.dateBeginLease = dateBeginLease;
	}

	public Date getDateEndLease() {
		return dateEndLease;
	}

	public void setDateEndLease(Date dateEndLease) {
		this.dateEndLease = dateEndLease;
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
		return "PledgeSubjectRealtyLandLease{" +
				"permittedUse='" + permittedUse + '\'' +
				", builtUp='" + builtUp + '\'' +
				", cadastralNumOfBuilding='" + cadastralNumOfBuilding + '\'' +
				", dateBeginLease=" + dateBeginLease +
				", dateEndLease=" + dateEndLease +
				'}';
	}
}
