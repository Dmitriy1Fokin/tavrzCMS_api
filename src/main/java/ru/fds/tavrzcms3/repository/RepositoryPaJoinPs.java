package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.jointable.PaJoinPs;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.util.List;


public interface RepositoryPaJoinPs extends JpaRepository<PaJoinPs, Long> {
    void deleteByPledgeAgreementAndPledgeSubject(PledgeAgreement pledgeAgreement, PledgeSubject pledgeSubject);
    void deleteByPledgeSubject(PledgeSubject pledgeSubject);
    void deleteByPledgeAgreementInAndPledgeSubject(List<PledgeAgreement> pledgeAgreementList, PledgeSubject pledgeSubject);
    boolean existsByPledgeAgreementAndPledgeSubject(PledgeAgreement pledgeAgreement, PledgeSubject pledgeSubject);
}
