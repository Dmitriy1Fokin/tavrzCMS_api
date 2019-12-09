package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;

import java.util.Date;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "pledge_realty_land_lease")
@SecondaryTable(name = "pledge_realty_prime", pkJoinColumns = @PrimaryKeyJoinColumn(name = "pledge_subject_id"))
public class PledgeSubjectLandLease extends PledgeSubject {

    @NotNull(message = "Обязательно для заполнения")
    @PositiveOrZero(message = "Значение должно быть больше или ровно нулю")
    @Column(name ="area", table = "pledge_realty_prime")
    private double area;

    @Pattern(regexp = "[0-9]{2}:[0-9]{2}:[0-9]{3,7}:[0-9]+",
            message = "Неверное значение")
    @Column(name ="cadastral_num", table = "pledge_realty_prime")
    private String cadastralNum;

    @Column(name ="conditional_num", table = "pledge_realty_prime")
    private String conditionalNum;

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
	@OneToOne(mappedBy = "pledgeSubjectLandLease")
	@JsonIgnore
	private PledgeSubject pledgeSubject;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "land_category_id")
	@JsonIgnore
	private LandCategory landCategory;

	public PledgeSubjectLandLease(){
		super.setTypeOfCollateral(TypeOfCollateral.LAND_LEASE);
	}

	@Override
	public String toString() {
		return "PledgeSubjectLandLease{" +
				"permittedUse='" + permittedUse + '\'' +
				", builtUp='" + builtUp + '\'' +
				", cadastralNumOfBuilding='" + cadastralNumOfBuilding + '\'' +
				", dateBeginLease=" + dateBeginLease +
				", dateEndLease=" + dateEndLease +
				'}';
	}
}
