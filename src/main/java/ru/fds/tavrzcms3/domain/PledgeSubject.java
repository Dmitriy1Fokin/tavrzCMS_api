package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

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
	private long pledgeSubjectId;

    @NotBlank(message = "Обязательно для заполнения")
	@Column(name ="name")
	private String name;

    @Pattern(regexp = "высокая|средняя|ниже средней|низкая",
            message = "Возможные варианты: высокая, средняя, ниже средней, низкая")
	@Column(name ="liquidity")
	private String liquidity;

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

    @Pattern(regexp = "в наличии|утрата|частичная утрата",
            message = "Возможные варианты: в наличии, утрата, частичная утрата")
	@Column(name ="status_monitoring")
	private String statusMonitoring;

    @Pattern(regexp = "Авто/спецтехника|Оборудование|ТМЦ|Ценные бумаги|Недвижимость - ЗУ - собственность|" +
            "Недвижимость - ЗУ - право аренды|Недвижимость - здание/сооружение|Недвижимость - помещение|Судно",
            message = "Возможные варианты: Авто/спецтехника, Оборудование, ТМЦ, Ценные бумаги, Недвижимость - ЗУ - собственность, " +
                    "Недвижимость - ЗУ - право аренды, Недвижимость - здание/сооружение, Недвижимость - помещение, Судно")
	@Column(name ="type_of_collateral")
	private String typeOfCollateral;

    @Pattern(regexp = "возвратная|рычаговая|ограничивающая|информационная",
            message = "Возможные варианты: возвратная, рычаговая, ограничивающая, информационная")
	@Column(name ="type_of_pledge")
	private String typeOfPledge;

    @Pattern(regexp = "документарный|визуальный",
            message = "Возможные варианты: документарный, визуальный")
	@Column(name ="type_of_monitoring")
	private String typeOfMonitoring;

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
	private PledgeSubjectRealty pledgeSubjectRealty;
	
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
