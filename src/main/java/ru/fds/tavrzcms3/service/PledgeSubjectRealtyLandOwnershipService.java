package ru.fds.tavrzcms3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.PledgeSubjectRealtyLandOwnership;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubjectRealtyLandOwnership;

@Service
public class PledgeSubjectRealtyLandOwnershipService {

    @Autowired
    RepositoryPledgeSubjectRealtyLandOwnership repositoryPledgeSubjectRealtyLandOwnership;

    public PledgeSubjectRealtyLandOwnership getPledgeSubjectRealtyLandOwnership(long pledgeSubjectId){
        return repositoryPledgeSubjectRealtyLandOwnership.getOne(pledgeSubjectId);
    }

    @Transactional
    public PledgeSubjectRealtyLandOwnership updatePledgeSubjectRealtyLandOwnership(PledgeSubjectRealtyLandOwnership pledgeSubjectRealtyLandOwnership){
        return repositoryPledgeSubjectRealtyLandOwnership.save(pledgeSubjectRealtyLandOwnership);
    }
}
