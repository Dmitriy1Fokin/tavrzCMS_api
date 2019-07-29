package ru.fds.tavrzcms3.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "pledge_subject")
@Inheritance(strategy = InheritanceType.JOINED)
public class PledgeSubject {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="pledge_subject_id")
	private long pledgeSubjectId;
	
	@Column(name ="name")
	private String name;

	@Column(name ="liquidity")
	private String liquidity;

	@Column(name ="zs_dz")
	private double zsDz;

	@Column(name ="zs_zz")
	private double zsZz;

	@Column(name ="rs_dz")
	private double rsDz;

	@Column(name ="rs_zz")
	private double rsZz;

	@Column(name ="ss")
	private double ss;

	@Column(name ="date_monitoring")
	@Temporal(TemporalType.DATE)
	private Date dateMonitoring;

	@Column(name ="date_conclusion")
	@Temporal(TemporalType.DATE)
	private Date dateConclusion;

	@Column(name ="status_monitoring")
	private String statusMonitoring;

	@Column(name ="type_of_collateral")
	private String typeOfCollateral;

	@Column(name ="type_of_pledge")
	private String typeOfPledge;

	@Column(name ="type_of_monitoring")
	private String typeOfMonitoring;

	@Column(name ="adress_region")
	private String adressRegion;

	@Column(name ="adress_district")
	private String adressDistrict;

	@Column(name ="adress_city")
	private String adressCity;

	@Column(name ="adress_street")
	private String adressStreet;

	@Column(name ="adress_building")
	private String adressBuilbing;

	@Column(name ="adress_premises")
	private String adressPemises;

	@Column(name ="insurance_obligation")
	private String insuranceObligation;
	
	@ManyToMany
	@JoinTable(name = "dz_ps", joinColumns = @JoinColumn(name ="pledge_subject_id"), inverseJoinColumns = @JoinColumn(name ="dz_id"))
	private List<PledgeEgreement> pledgeEgreements;
	
	@OneToMany(mappedBy = "pledgeSubject")
	private List<CostHistory> costHistories;
	
	@OneToMany(mappedBy = "pledgeSubject")
	private List<Monitoring> monitorings;

	@OneToMany(mappedBy = "pledgeSubject")
	private List<Encumbrance> encumbrances;

    @OneToMany(mappedBy = "pledgeSubject")
    private List<Insurance> insurances;
	
	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
	private PledgeSubjectAuto pledgeSubjectAuto;
	
	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
	private PledgeSubjectEquipment pledgeSubjectEquipment;
	
	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
	private PledgeSubjectRealty pledgeSubjectRealty;
	
	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
	private PledgeSubjectSecurities pledgeSubjectSecurities;
	
	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
	private PledgeSubjectTBO pledgeSubjectTBO;
	
	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
	private PledgeSubjectVessel pledgeSubjectVessel;

    public long getPledgeSubjectId() {
        return pledgeSubjectId;
    }

    public void setPledgeSubjectId(long pledgeSubjectId) {
        this.pledgeSubjectId = pledgeSubjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLiquidity() {
        return liquidity;
    }

    public void setLiquidity(String liquidity) {
        this.liquidity = liquidity;
    }

    public double getZsDz() {
        return zsDz;
    }

    public void setZsDz(double zsDz) {
        this.zsDz = zsDz;
    }

    public double getZsZz() {
        return zsZz;
    }

    public void setZsZz(double zsZz) {
        this.zsZz = zsZz;
    }

    public double getRsDz() {
        return rsDz;
    }

    public void setRsDz(double rsDz) {
        this.rsDz = rsDz;
    }

    public double getRsZz() {
        return rsZz;
    }

    public void setRsZz(double rsZz) {
        this.rsZz = rsZz;
    }

    public double getSs() {
        return ss;
    }

    public void setSs(double ss) {
        this.ss = ss;
    }

    public Date getDateMonitoring() {
        return dateMonitoring;
    }

    public void setDateMonitoring(Date dateMonitoring) {
        this.dateMonitoring = dateMonitoring;
    }

    public Date getDateConclusion() {
        return dateConclusion;
    }

    public void setDateConclusion(Date dateConclusion) {
        this.dateConclusion = dateConclusion;
    }

    public String getStatusMonitoring() {
        return statusMonitoring;
    }

    public void setStatusMonitoring(String statusMonitoring) {
        this.statusMonitoring = statusMonitoring;
    }

    public String getTypeOfCollateral() {
        return typeOfCollateral;
    }

    public void setTypeOfCollateral(String typeOfCollateral) {
        this.typeOfCollateral = typeOfCollateral;
    }

    public String getTypeOfPledge() {
        return typeOfPledge;
    }

    public void setTypeOfPledge(String typeOfPledge) {
        this.typeOfPledge = typeOfPledge;
    }

    public String getTypeOfMonitoring() {
        return typeOfMonitoring;
    }

    public void setTypeOfMonitoring(String typeOfMonitoring) {
        this.typeOfMonitoring = typeOfMonitoring;
    }

    public String getAdressRegion() {
        return adressRegion;
    }

    public void setAdressRegion(String adressRegion) {
        this.adressRegion = adressRegion;
    }

    public String getAdressDistrict() {
        return adressDistrict;
    }

    public void setAdressDistrict(String adressDistrict) {
        this.adressDistrict = adressDistrict;
    }

    public String getAdressCity() {
        return adressCity;
    }

    public void setAdressCity(String adressCity) {
        this.adressCity = adressCity;
    }

    public String getAdressStreet() {
        return adressStreet;
    }

    public void setAdressStreet(String adressStreet) {
        this.adressStreet = adressStreet;
    }

    public String getAdressBuilbing() {
        return adressBuilbing;
    }

    public void setAdressBuilbing(String adressBuilbing) {
        this.adressBuilbing = adressBuilbing;
    }

    public String getAdressPemises() {
        return adressPemises;
    }

    public void setAdressPemises(String adressPemises) {
        this.adressPemises = adressPemises;
    }

    public String getInsuranceObligation() {
        return insuranceObligation;
    }

    public void setInsuranceObligation(String insuranceObligation) {
        this.insuranceObligation = insuranceObligation;
    }

    public List<PledgeEgreement> getPledgeEgreements() {
        return pledgeEgreements;
    }

    public void setPledgeEgreements(List<PledgeEgreement> pledgeEgreements) {
        this.pledgeEgreements = pledgeEgreements;
    }

    public List<CostHistory> getCostHistories() {
        return costHistories;
    }

    public void setCostHistories(List<CostHistory> costHistories) {
        this.costHistories = costHistories;
    }

    public List<Monitoring> getMonitorings() {
        return monitorings;
    }

    public void setMonitorings(List<Monitoring> monitorings) {
        this.monitorings = monitorings;
    }

    public List<Encumbrance> getEncumbrances() {
        return encumbrances;
    }

    public void setEncumbrances(List<Encumbrance> encumbrances) {
        this.encumbrances = encumbrances;
    }

    public List<Insurance> getInsurances() {
        return insurances;
    }

    public void setInsurances(List<Insurance> insurances) {
        this.insurances = insurances;
    }

    public PledgeSubjectAuto getPledgeSubjectAuto() {
        return pledgeSubjectAuto;
    }

    public void setPledgeSubjectAuto(PledgeSubjectAuto pledgeSubjectAuto) {
        this.pledgeSubjectAuto = pledgeSubjectAuto;
    }

    public PledgeSubjectEquipment getPledgeSubjectEquipment() {
        return pledgeSubjectEquipment;
    }

    public void setPledgeSubjectEquipment(PledgeSubjectEquipment pledgeSubjectEquipment) {
        this.pledgeSubjectEquipment = pledgeSubjectEquipment;
    }

    public PledgeSubjectRealty getPledgeSubjectRealty() {
        return pledgeSubjectRealty;
    }

    public void setPledgeSubjectRealty(PledgeSubjectRealty pledgeSubjectRealty) {
        this.pledgeSubjectRealty = pledgeSubjectRealty;
    }

    public PledgeSubjectSecurities getPledgeSubjectSecurities() {
        return pledgeSubjectSecurities;
    }

    public void setPledgeSubjectSecurities(PledgeSubjectSecurities pledgeSubjectSecurities) {
        this.pledgeSubjectSecurities = pledgeSubjectSecurities;
    }

    public PledgeSubjectTBO getPledgeSubjectTBO() {
        return pledgeSubjectTBO;
    }

    public void setPledgeSubjectTBO(PledgeSubjectTBO pledgeSubjectTBO) {
        this.pledgeSubjectTBO = pledgeSubjectTBO;
    }

    public PledgeSubjectVessel getPledgeSubjectVessel() {
        return pledgeSubjectVessel;
    }

    public void setPledgeSubjectVessel(PledgeSubjectVessel pledgeSubjectVessel) {
        this.pledgeSubjectVessel = pledgeSubjectVessel;
    }

    @Override
    public String toString() {
        return "PledgeSubject{" +
                "pledgeSubjectId=" + pledgeSubjectId +
                ", name='" + name + '\'' +
                ", liquidity='" + liquidity + '\'' +
                ", zsDz=" + zsDz +
                ", zsZz=" + zsZz +
                ", rsDz=" + rsDz +
                ", rsZz=" + rsZz +
                ", ss=" + ss +
                ", dateMonitoring=" + dateMonitoring +
                ", dateConclusion=" + dateConclusion +
                ", statusMonitoring='" + statusMonitoring + '\'' +
                ", typeOfCollateral='" + typeOfCollateral + '\'' +
                ", typeOfPledge='" + typeOfPledge + '\'' +
                ", typeOfMonitoring='" + typeOfMonitoring + '\'' +
                ", adressRegion='" + adressRegion + '\'' +
                ", adressDistrict='" + adressDistrict + '\'' +
                ", adressCity='" + adressCity + '\'' +
                ", adressStreet='" + adressStreet + '\'' +
                ", adressBuilbing='" + adressBuilbing + '\'' +
                ", adressPemises='" + adressPemises + '\'' +
                ", insuranceObligation='" + insuranceObligation + '\'' +
                '}';
    }
}
