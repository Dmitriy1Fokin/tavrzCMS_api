package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fds.tavrzcms3.dictionary.StatusOfAgreement;
import ru.fds.tavrzcms3.dictionary.TypeOfPledgeAgreement;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dz")
public class PledgeAgreement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="dz_id")
	private long pledgeAgreementId;

	@NotBlank(message = "Обязательно для заполнения")
	@Column(name ="num_dz")
	private String numPA;

	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="date_begin_dz")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateBeginPA;

	@NotNull(message = "Обязательно для заполнения")
	@Column(name ="date_end_dz")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateEndPA;

	@Convert(converter = TypeOfPledgeAgreement.Converter.class)
	@Column(name ="perv_posl")
	private TypeOfPledgeAgreement pervPosl;

	@Convert(converter = StatusOfAgreement.Converter.class)
	@Column(name ="status")
	private StatusOfAgreement statusPA;
	
	@Column(name ="notice")
	private String noticePA;

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

	@Singular
	@JsonIgnore
	@ManyToMany()
	@JoinTable(name = "kd_dz", joinColumns = @JoinColumn(name ="dz_id"), inverseJoinColumns = @JoinColumn(name ="kd_id"))
	private List<LoanAgreement> loanAgreements;

	@JsonIgnore
	@ManyToOne()
	@JoinColumn(name = "pledgor_id")
	private Client client;

	@Singular
	@JsonIgnore
	@ManyToMany()
	@JoinTable(name = "dz_ps", joinColumns = @JoinColumn(name ="dz_id"), inverseJoinColumns = @JoinColumn(name ="pledge_subject_id"))
	private List<PledgeSubject> pledgeSubjects;

	@Override
	public String toString() {
		return "PledgeAgreement{" +
				"pledgeAgreementId=" + pledgeAgreementId +
				", numPA='" + numPA + '\'' +
				", dateBeginPA=" + dateBeginPA +
				", dateEndPA=" + dateEndPA +
				", pervPosl='" + pervPosl + '\'' +
				", statusPA='" + statusPA + '\'' +
				", noticePA='" + noticePA + '\'' +
				", zsDz=" + zsDz +
				", zsZz=" + zsZz +
				", rsDz=" + rsDz +
				", rsZz=" + rsZz +
				", ss=" + ss +
				'}';
	}
}
