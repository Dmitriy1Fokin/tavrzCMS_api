package ru.fds.tavrzcms3.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.dictionary.TypeOfTBO;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode(callSuper = true)
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

	@Convert(converter = TypeOfTBO.Converter.class)
	@Column(name ="type_of_tbo")
	private TypeOfTBO typeOfTBO;

	@Valid
	@OneToOne(mappedBy = "pledgeSubjectTBO")
	@JsonIgnore
	private PledgeSubject pledgeSubject;

	public PledgeSubjectTBO(){
		super.setTypeOfCollateral(TypeOfCollateral.TBO);
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
