package ru.fds.tavrzcms3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.PledgeSubjectVessel;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubjectVessel;

@Service
public class PledgeSubjectVesselService {

    @Autowired
    RepositoryPledgeSubjectVessel repositoryPledgeSubjectVessel;

    public PledgeSubjectVessel getPledgeSubjectVessel(long pledgeSubjectId){
        return repositoryPledgeSubjectVessel.getOne(pledgeSubjectId);
    }

    @Transactional
    public PledgeSubjectVessel updatePledgeSubjectVessel(PledgeSubjectVessel pledgeSubjectVessel){
        return repositoryPledgeSubjectVessel.save(pledgeSubjectVessel);
    }
}
