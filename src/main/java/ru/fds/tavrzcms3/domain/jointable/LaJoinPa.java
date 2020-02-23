package ru.fds.tavrzcms3.domain.jointable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "kd_dz")
public class LaJoinPa {

    @Id
    @SequenceGenerator(name = "kd_dz_sequence", sequenceName = "kd_dz_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kd_dz_sequence")
    @Column(name ="id")
    private Long laPaid;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kd_id")
    private LoanAgreement loanAgreement;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dz_id")
    private PledgeAgreement pledgeAgreement;

    public LaJoinPa(LoanAgreement loanAgreement, PledgeAgreement pledgeAgreement){
        this.loanAgreement = loanAgreement;
        this.pledgeAgreement = pledgeAgreement;
    }

    @Override
    public String toString() {
        return "LaJoinPa{" +
                "laPaid=" + laPaid +
                '}';
    }
}
