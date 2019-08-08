package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.repository.RepositoryPledgeAgreement;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;

import java.util.List;

@Service
public class PledgeSubjectService {

    @Autowired
    RepositoryPledgeSubject repositoryPledgeSubject;

    @Autowired
    RepositoryPledgeAgreement repositoryPledgeAgreement;

    public synchronized PledgeSubject getPledgeSubjectById(long id){
        PledgeSubject pledgeSubject = repositoryPledgeSubject.findByPledgeSubjectId(id);
        return  pledgeSubject;
    }

    public synchronized List<PledgeSubject> getPledgeSubjectsForPledgeAgreement(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        return repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
    }
}
