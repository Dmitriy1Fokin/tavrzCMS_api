package ru.fds.tavrzcms3.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.Liquidity;
import ru.fds.tavrzcms3.dictionary.StatusOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.dictionary.TypeOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfPledge;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectAuto;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectBuilding;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectEquipment;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectLandLease;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectLandOwnership;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectRoom;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectSecurities;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectTBO;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectVessel;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pledge_subject")
@SecondaryTables({
        @SecondaryTable(name = "pledge_auto", pkJoinColumns = @PrimaryKeyJoinColumn(name = "pledge_subject_id")),
        @SecondaryTable(name = "pledge_realty_building", pkJoinColumns = @PrimaryKeyJoinColumn(name = "pledge_subject_id")),
        @SecondaryTable(name = "pledge_realty_land_lease", pkJoinColumns = @PrimaryKeyJoinColumn(name = "pledge_subject_id")),
        @SecondaryTable(name = "pledge_realty_land_ownership", pkJoinColumns = @PrimaryKeyJoinColumn(name = "pledge_subject_id")),
        @SecondaryTable(name = "pledge_realty_room", pkJoinColumns = @PrimaryKeyJoinColumn(name = "pledge_subject_id")),
        @SecondaryTable(name = "pledge_equipment", pkJoinColumns = @PrimaryKeyJoinColumn(name = "pledge_subject_id")),
        @SecondaryTable(name = "pledge_securities", pkJoinColumns = @PrimaryKeyJoinColumn(name = "pledge_subject_id")),
        @SecondaryTable(name = "pledge_tbo", pkJoinColumns = @PrimaryKeyJoinColumn(name = "pledge_subject_id")),
        @SecondaryTable(name = "pledge_vessel", pkJoinColumns = @PrimaryKeyJoinColumn(name = "pledge_subject_id"))
})
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
	private BigDecimal zsDz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="zs_zz")
	private BigDecimal zsZz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="rs_dz")
	private BigDecimal rsDz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="rs_zz")
	private BigDecimal rsZz;

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
	@Column(name ="ss")
	private BigDecimal ss;

    @NotNull(message = "Обязательно для заполнения")
	@Column(name ="date_monitoring", columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateMonitoring;

    @NotNull(message = "Обязательно для заполнения")
	@Column(name ="date_conclusion", columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateConclusion;

	@NotNull(message = "Обязательно для заполнения")
    @Convert(converter = StatusOfMonitoring.Converter.class)
	@Column(name ="status_monitoring")
	private StatusOfMonitoring statusMonitoring;

	@NotNull(message = "Обязательно для заполнения")
    @Convert(converter = TypeOfCollateral.Converter.class)
	@Column(name ="type_of_collateral")
	private TypeOfCollateral typeOfCollateral;

	@NotNull(message = "Обязательно для заполнения")
    @Convert(converter = TypeOfPledge.Converter.class)
	@Column(name ="type_of_pledge")
	private TypeOfPledge typeOfPledge;

	@NotNull(message = "Обязательно для заполнения")
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

    @Valid
	@Embedded
	private PledgeSubjectAuto pledgeSubjectAuto;

    @Valid
	@Embedded
	private PledgeSubjectEquipment pledgeSubjectEquipment;

    @Valid
    @Embedded
	private PledgeSubjectBuilding pledgeSubjectBuilding;

    @Valid
    @Embedded
	private PledgeSubjectLandLease pledgeSubjectLandLease;

    @Valid
	@Embedded
	private PledgeSubjectLandOwnership pledgeSubjectLandOwnership;

    @Valid
    @Embedded
	private PledgeSubjectRoom pledgeSubjectRoom;

    @Valid
    @Embedded
	private PledgeSubjectSecurities pledgeSubjectSecurities;
	
    @Valid
    @Embedded
	private PledgeSubjectTBO pledgeSubjectTBO;
	
    @Valid
	@Embedded
	private PledgeSubjectVessel pledgeSubjectVessel;

    @Override
    public String toString() {
        return "PledgeSubject{" +
                "pledgeSubjectId=" + pledgeSubjectId +
                ", name='" + name + '\'' +
                ", liquidity=" + liquidity +
                ", zsDz=" + zsDz +
                ", zsZz=" + zsZz +
                ", rsDz=" + rsDz +
                ", rsZz=" + rsZz +
                ", ss=" + ss +
                ", dateMonitoring=" + dateMonitoring +
                ", dateConclusion=" + dateConclusion +
                ", statusMonitoring=" + statusMonitoring +
                ", typeOfCollateral=" + typeOfCollateral +
                ", typeOfPledge=" + typeOfPledge +
                ", typeOfMonitoring=" + typeOfMonitoring +
                ", adressRegion='" + adressRegion + '\'' +
                ", adressDistrict='" + adressDistrict + '\'' +
                ", adressCity='" + adressCity + '\'' +
                ", adressStreet='" + adressStreet + '\'' +
                ", adressBuilbing='" + adressBuilbing + '\'' +
                ", adressPemises='" + adressPemises + '\'' +
                ", insuranceObligation='" + insuranceObligation + '\'' +
                ", pledgeSubjectAuto=" + pledgeSubjectAuto +
                ", pledgeSubjectEquipment=" + pledgeSubjectEquipment +
                ", pledgeSubjectBuilding=" + pledgeSubjectBuilding +
                ", pledgeSubjectLandLease=" + pledgeSubjectLandLease +
                ", pledgeSubjectLandOwnership=" + pledgeSubjectLandOwnership +
                ", pledgeSubjectRoom=" + pledgeSubjectRoom +
                ", pledgeSubjectSecurities=" + pledgeSubjectSecurities +
                ", pledgeSubjectTBO=" + pledgeSubjectTBO +
                ", pledgeSubjectVessel=" + pledgeSubjectVessel +
                '}';
    }
}
