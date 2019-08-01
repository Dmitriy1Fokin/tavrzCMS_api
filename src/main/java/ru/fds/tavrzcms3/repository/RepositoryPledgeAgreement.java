package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.util.Date;
import java.util.List;

public interface RepositoryPledgeEgreement extends JpaRepository<PledgeAgreement, Long> {
    List<PledgeAgreement> findByNumPE (String numPE);
    List<PledgeAgreement> findByDateBeginPE (Date dateBeginPE);
    List<PledgeAgreement> findByDateEndPE (Date dateEndPE);
    List<PledgeAgreement> findByPervPosl (String pervPosl);
    List<PledgeAgreement> findByPledgor (Client pledgor);
    List<PledgeAgreement> findByStatusPE (String statusPE);
    List<PledgeAgreement> findByPledgorIn (List<Client> pledgors);
    int countAllByPledgorIn(List<Client> pledgors);
    int countAllByPledgorInAndPervPoslEquals(List<Client> pledgors, String perv);
    PledgeAgreement findByPledgeSubjects(PledgeSubject pledgeSubject);
}
