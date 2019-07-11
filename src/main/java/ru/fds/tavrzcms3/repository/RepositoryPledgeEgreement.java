package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.PledgeEgreement;

import java.util.Date;
import java.util.List;

public interface RepositoryPledgeEgreement extends JpaRepository<PledgeEgreement, Long> {
    List<PledgeEgreement> findByNumPE (String numPE);
    List<PledgeEgreement> findByDateBeginPE (Date dateBeginPE);
    List<PledgeEgreement> findByDateEndPE (Date dateEndPE);
    List<PledgeEgreement> findByPervPosl (String pervPosl);
    List<PledgeEgreement> findByPledgor (Client pledgor);
    List<PledgeEgreement> findByStatusPE (String statusPE);
    List<PledgeEgreement> findByPledgorIn (List<Client> pledgors);
    int countAllByPledgorIn(List<Client> pledgors);
}
