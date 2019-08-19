package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.PledgeSubjectRealtyBuilding;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubjectRealtyBuilding;

@Service
public class PledgeSubjectRealtyBuildingService {

    @Autowired
    RepositoryPledgeSubjectRealtyBuilding repositoryPledgeSubjectRealtyBuilding;

    public PledgeSubjectRealtyBuilding getPledgeSubjectRealtyBuilding(long pledgeSubjectId){
        return repositoryPledgeSubjectRealtyBuilding.getOne(pledgeSubjectId);
    }

    @Transactional
    public PledgeSubjectRealtyBuilding updatePledgeSubjectRealtyBuilding(PledgeSubjectRealtyBuilding pledgeSubjectRealtyBuilding){
        return repositoryPledgeSubjectRealtyBuilding.save(pledgeSubjectRealtyBuilding);
    }
}
