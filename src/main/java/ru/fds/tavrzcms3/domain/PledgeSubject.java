package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.*;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "pledge_subject")
@Inheritance(strategy = InheritanceType.JOINED)
public class PledgeSubject {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="pledge_subject_id")
	private Long pledgeSubjectId;

    @NotBlank(message = "Обязательно для заполнения")
	@Column(name ="name")
	private String name;

    @Convert(converter = Liquidity.Converter.class)
	@Column(name ="liquidity")
	private Liquidity liquidity;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="zs_dz")
	private double zsDz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="zs_zz")
	private double zsZz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="rs_dz")
	private double rsDz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="rs_zz")
	private double rsZz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="ss")
	private double ss;

    @NotNull(message = "Обязательно для заполнения")
	@Column(name ="date_monitoring")
	@Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateMonitoring;

    @NotNull(message = "Обязательно для заполнения")
	@Column(name ="date_conclusion")
	@Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateConclusion;

    @Convert(converter = StatusOfMonitoring.Converter.class)
	@Column(name ="status_monitoring")
	private StatusOfMonitoring statusMonitoring;

    @Convert(converter = TypeOfCollateral.Converter.class)
	@Column(name ="type_of_collateral")
	private TypeOfCollateral typeOfCollateral;

    @Convert(converter = TypeOfPledge.Converter.class)
	@Column(name ="type_of_pledge")
	private TypeOfPledge typeOfPledge;

    @Convert(converter = TypeOfMonitoring.Converter.class)
	@Column(name ="type_of_monitoring")
	private TypeOfMonitoring typeOfMonitoring;

    @NotBlank(message = "Обязательно для заполнения")
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

    @Pattern(regexp = "да|нет", message = "Возможные варианты: да, нет")
	@Column(name ="insurance_obligation")
	private String insuranceObligation;

    @Singular
	@ManyToMany
	@JoinTable(name = "dz_ps", joinColumns = @JoinColumn(name ="pledge_subject_id"), inverseJoinColumns = @JoinColumn(name ="dz_id"))
    @JsonIgnore
	private List<PledgeAgreement> pledgeAgreements;

    @Singular
	@OneToMany(mappedBy = "pledgeSubject")
    @JsonIgnore
    private List<CostHistory> costHistories;

    @Singular
	@OneToMany(mappedBy = "pledgeSubject")
    @JsonIgnore
    private List<Monitoring> monitorings;

    @Singular
	@OneToMany(mappedBy = "pledgeSubject")
    @JsonIgnore
    private List<Encumbrance> encumbrances;

    @Singular
    @OneToMany(mappedBy = "pledgeSubject")
    @JsonIgnore
    private List<Insurance> insurances;
	
	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
    @JsonIgnore
	private PledgeSubjectAuto pledgeSubjectAuto;
	
	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
    @JsonIgnore
	private PledgeSubjectEquipment pledgeSubjectEquipment;

	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
	@JsonIgnore
	private PledgeSubjectRealtyBuilding pledgeSubjectRealtyBuilding;

	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
	@JsonIgnore
	private PledgeSubjectRealtyLandLease pledgeSubjectRealtyLandLease;

	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
	@JsonIgnore
	private PledgeSubjectRealtyLandOwnership pledgeSubjectRealtyLandOwnership;

	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
	@JsonIgnore
	private PledgeSubjectRealtyRoom pledgeSubjectRealtyRoom;
	
	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
    @JsonIgnore
	private PledgeSubjectSecurities pledgeSubjectSecurities;
	
	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
    @JsonIgnore
	private PledgeSubjectTBO pledgeSubjectTBO;
	
	@OneToOne
	@JoinColumn(name = "pledgeSubject_id")
    @JsonIgnore
	private PledgeSubjectVessel pledgeSubjectVessel;

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
