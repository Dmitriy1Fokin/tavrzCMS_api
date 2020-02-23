package ru.fds.tavrzcms3.domain.jointable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;

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
@Table(name = "dz_ps")
public class PaJoinPs {

    @Id
    @SequenceGenerator(name = "dz_ps_sequence", sequenceName = "dz_ps_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dz_ps_sequence")
    @Column(name ="id")
    private Long paPsId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dz_id")
    private PledgeAgreement pledgeAgreement;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pledge_subject_id")
    private PledgeSubject pledgeSubject;

    public PaJoinPs(PledgeAgreement pledgeAgreement, PledgeSubject pledgeSubject){
        this.pledgeAgreement = pledgeAgreement;
        this.pledgeSubject = pledgeSubject;
    }

    @Override
    public String toString() {
        return "PaJoinPs{" +
                "paPsId=" + paPsId +
                '}';
    }
}
