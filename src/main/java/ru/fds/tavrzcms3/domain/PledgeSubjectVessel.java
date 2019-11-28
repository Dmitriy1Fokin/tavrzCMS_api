package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.dictionary.TypeOfVessel;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "pledge_vessel")
public class PledgeSubjectVessel extends PledgeSubject {

	@Length(min = 7, max = 7, message = "Неверное значение")
	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="imo")
	private Integer imo;

	@Length(min = 9, max = 9, message = "Неверное значение")
	@Column(name ="mmsi")
	private Integer mmsi;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="flag")
	private String flag;

	@Convert(converter = TypeOfVessel.Converter.class)
	@Column(name ="vessel_type")
	private TypeOfVessel vesselType;

	@NotNull(message = "Обязательно для заполнения")
	@Positive(message = "Значение должно быть больше нуля")
	@Column(name ="gross_tonnage")
	private int grossTonnage;

	@NotNull(message = "Обязательно для заполнения")
	@Positive(message = "Значение должно быть больше нуля")
	@Column(name ="deadweight")
	private int deadweight;

	@Min(value = 1900, message = "Неверное значение")
	@Max(value = 2100, message = "Неверное значение")
	@Length(min = 4, max = 4, message = "Неверное значение")
	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="year_built")
	private int yearBuilt;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="status")
	private String statusVessel;

	@Valid
	@OneToOne(mappedBy = "pledgeSubjectVessel")
	@JsonIgnore
	private PledgeSubject pledgeSubject;

	public PledgeSubjectVessel(){
		super.setTypeOfCollateral(TypeOfCollateral.VESSEL);
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
