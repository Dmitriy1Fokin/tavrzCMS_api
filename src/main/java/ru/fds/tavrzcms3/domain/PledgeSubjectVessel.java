package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pledge_vessel")
public class PledgeSubjectVessel extends PledgeSubject {
	
	@Column(name ="imo")
	private int imo;
	
	@Column(name ="mmsi")
	private Integer mmsi;
	
	@Column(name ="flag")
	private String flag;
	
	@Column(name ="vessel_type")
	private String vesselType;
	
	@Column(name ="gross_tonnage")
	private int grossTonnage;
	
	@Column(name ="deadweight")
	private int deadweight;
	
	@Column(name ="year_built")
	private int yearBuilt;
	
	@Column(name ="status")
	private String statusVessel;
	
	@OneToOne(mappedBy = "pledgeSubjectVessel")
	@JsonIgnore
	private PledgeSubject pledgeSubject;

	public PledgeSubjectVessel(){
		super.setTypeOfCollateral("Судно");
	}

	public int getImo() {
		return imo;
	}

	public void setImo(int imo) {
		this.imo = imo;
	}

	public Integer getMmsi() {
		return mmsi;
	}

	public void setMmsi(Integer mmsi) {
		this.mmsi = mmsi;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getVesselType() {
		return vesselType;
	}

	public void setVesselType(String vesselType) {
		this.vesselType = vesselType;
	}

	public int getGrossTonnage() {
		return grossTonnage;
	}

	public void setGrossTonnage(int grossTonnage) {
		this.grossTonnage = grossTonnage;
	}

	public int getDeadweight() {
		return deadweight;
	}

	public void setDeadweight(int deadweight) {
		this.deadweight = deadweight;
	}

	public int getYearBuilt() {
		return yearBuilt;
	}

	public void setYearBuilt(int yearBuilt) {
		this.yearBuilt = yearBuilt;
	}

	public String getStatusVessel() {
		return statusVessel;
	}

	public void setStatusVessel(String statusVessel) {
		this.statusVessel = statusVessel;
	}

	public PledgeSubject getPledgeSubject() {
		return pledgeSubject;
	}

	public void setPledgeSubject(PledgeSubject pledgeSubject) {
		this.pledgeSubject = pledgeSubject;
	}

	@Override
	public String toString() {
		return "PledgeSubjectVessel{" +
				"imo=" + imo +
				", mmsi=" + mmsi +
				", flag='" + flag + '\'' +
				", vesselType='" + vesselType + '\'' +
				", grossTonnage=" + grossTonnage +
				", deadweight=" + deadweight +
				", yearBuilt=" + yearBuilt +
				", statusVessel='" + statusVessel + '\'' +
				'}';
	}
}
