package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.PledgeSubjectRealtyLandLease;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubjectRealtyLandLease;

@Service
public class PledgeSubjectRealtyLandLeaseService {

    @Autowired
    RepositoryPledgeSubjectRealtyLandLease repositoryPledgeSubjectRealtyLandLease;

    public PledgeSubjectRealtyLandLease getPledgeSubjectRealtyLandLease(long pledgeSubjectId){
        return repositoryPledgeSubjectRealtyLandLease.getOne(pledgeSubjectId);
    }

    @Transactional
    public PledgeSubjectRealtyLandLease updatePledgeSubjectRealtyLandLease(PledgeSubjectRealtyLandLease pledgeSubjectRealtyLandLease){
        return repositoryPledgeSubjectRealtyLandLease.save(pledgeSubjectRealtyLandLease);
    }

}
