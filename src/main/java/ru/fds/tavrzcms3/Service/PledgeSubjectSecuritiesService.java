package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.PledgeSubjectSecurities;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubjectSecurities;

@Service
public class PledgeSubjectSecuritiesService {

    @Autowired
    RepositoryPledgeSubjectSecurities repositoryPledgeSubjectSecurities;

    public PledgeSubjectSecurities getPledgeSubjectSecurities(long pledgeSubjectId){
        return repositoryPledgeSubjectSecurities.getOne(pledgeSubjectId);
    }

    @Transactional
    public PledgeSubjectSecurities updatePledgeSubjectSecurities(PledgeSubjectSecurities pledgeSubjectSecurities){
        return repositoryPledgeSubjectSecurities.save(pledgeSubjectSecurities);
    }
}
