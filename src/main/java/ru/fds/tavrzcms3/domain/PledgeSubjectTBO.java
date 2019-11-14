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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "pledge_tbo")
public class PledgeSubjectTBO extends PledgeSubject {

	@Positive(message = "Значение должно быть больше нуля")
	@Column(name ="count_of_tbo")
	private int countOfTBO;

	@Positive(message = "Значение должно быть больше нуля")
	@Column(name ="carrying_amount")
	private double carryingAmount;

	@Pattern(regexp = "транспорт|запчасти|одежда|продукты питания|алкоголь|нефтехимия|металлопродукция|стройматериалы|" +
			"крс|мрс|медикаменты|сантехника",
			message = "Возможные варианты: транспорт, запчасти, одежда,продукты питания, алкоголь. нефтехимия, " +
					"металлопродукция, стройматериалы, крс, мрс, медикаменты, сантехника")
	@Column(name ="type_of_tbo")
	private String typeOfTBO;

	@Valid
	@OneToOne(mappedBy = "pledgeSubjectTBO")
	@JsonIgnore
	private PledgeSubject pledgeSubject;

	public PledgeSubjectTBO(){
		super.setTypeOfCollateral("ТМЦ");
	}

    @Override
    public String toString() {
        return "PledgeSubjectTBO{" +
                "countOfTBO=" + countOfTBO +
                ", carryingAmount=" + carryingAmount +
                ", typeOfTBO='" + typeOfTBO + '\'' +
                '}';
    }
}
