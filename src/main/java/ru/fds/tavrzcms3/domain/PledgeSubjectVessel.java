package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "pledge_vessel")
public class PledgeSubjectVessel extends PledgeSubject {

	@Min(value = 1000000, message = "Неверное значение")
	@Max(value = 9999999, message = "Неверное значение")
	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="imo")
	private Integer imo;

	@Min(value = 100000000, message = "Неверное значение")
	@Max(value = 999999999, message = "Неверное значение")
	@Column(name ="mmsi")
	private Integer mmsi;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="flag")
	private String flag;

	@Pattern(regexp = "general cargo \\(gcd\\)|container ship \\(con\\)|log carrier/timber \\(log\\)|ro-ro \\(r/r\\)|bulk carrier \\(b/c\\)|" +
			"ore/oil carrier \\(o/o\\)|oil/bulk/ore carrier \\(obo\\)|tanker product \\(tnp\\)|tanker crude \\(tnc\\)|" +
			"tanker storage \\(tns\\)|tanker vlcc/ulcc \\(tnv\\)|chemical tanker \\(chm\\)|lpg/lng carrier \\(gas\\)|" +
			"offshore supply vessel \\(osv\\)|heavy lift vessel \\(hvl\\)|survey vessel \\(srv\\)|passenger ship \\(pas\\)|" +
			"reefer \\(rfg\\)|livestock carrier \\(liv\\)|tug|fishing trawler \\(fsh\\)|dredger \\(drg\\)|м|м-сп|о|р|л",
			message = "Возможные варианты: general cargo (gcd), container ship (con), log carrier/timber (log), " +
					"ro-ro (r/r), bulk carrier (b/c), ore/oil carrier (o/o), oil/bulk/ore carrier (obo), " +
					"tanker product (tnp), tanker crude (tnc), tanker storage (tns), tanker vlcc/ulcc (tnv), " +
					"chemical tanker (chm), lpg/lng carrier (gas), offshore supply vessel (osv), heavy lift vessel (hvl), " +
					"survey vessel (srv), passenger ship (pas), reefer (rfg), livestock carrier (liv), tug, " +
					"fishing trawler (fsh), dredger (drg), м, м-сп, о, р, л")
	@Column(name ="vessel_type")
	private String vesselType;

	@NotNull(message = "Обязательно для заполнения")
	@Positive(message = "Значение должно быть больше нуля")
	@Column(name ="gross_tonnage")
	private int grossTonnage;

	@NotNull(message = "Обязательно для заполнения")
	@Positive(message = "Значение должно быть больше нуля")
	@Column(name ="deadweight")
	private int deadweight;

	@Min(value = 1000, message = "Неверное значение")
	@Max(value = 9999, message = "Неверное значение")
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
		super.setTypeOfCollateral("Судно");
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
